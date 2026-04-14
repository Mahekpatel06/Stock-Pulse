package com.ownProject.GINS.inventory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ownProject.GINS.dto.InventoryDTO;
import com.ownProject.GINS.jpa.InventoryRepository;
import com.ownProject.GINS.jpa.NotificationRepository;
import com.ownProject.GINS.jpa.ProductRepository;
import com.ownProject.GINS.jpa.TransactionRepository;
import com.ownProject.GINS.jpa.WareHouseRepository;
import com.ownProject.GINS.notification.Notification;
import com.ownProject.GINS.product.Product;
import com.ownProject.GINS.transaction.Transaction;
import com.ownProject.GINS.transaction.Transaction.Type;
import com.ownProject.GINS.wareHouse.WareHouse;

import jakarta.validation.Valid;

@Service
public class InventoryService {

	private JavaMailSender mailSender;
	private TransactionRepository transactionRepository;
	private NotificationRepository notificationRepo;
	private InventoryRepository inventoryRepository;
	private ProductRepository productRepository;
	private WareHouseRepository warehouseRepository;
//	private InventoryController inventoryController;

	public InventoryService(JavaMailSender mailSender, TransactionRepository transactionRepository,
			NotificationRepository notificationRepo, InventoryRepository inventoryRepository,
			ProductRepository productRepository, WareHouseRepository warehouseRepository) {
		super();
		this.mailSender = mailSender;
		this.transactionRepository = transactionRepository;
		this.notificationRepo = notificationRepo;
		this.inventoryRepository = inventoryRepository;
		this.productRepository = productRepository;
		this.warehouseRepository = warehouseRepository;
//		this.inventoryController = inventoryController;
	}

	
//	Calculation for STOCK method - private
	private Inventory subtractStock(UUID productId, Integer warehouseId, Integer qty) {

		Inventory inv = inventoryRepository.findByProduct_IdAndWareHouse_Id(productId, warehouseId);

		if (inv == null) {
			throw new RuntimeException("Product not found in this warehouse");
		}

		if (inv.getQuantity() < qty) {
			throw new RuntimeException("Insufficient stock! Only " + inv.getQuantity() + " available.");
		}

		inv.setQuantity(inv.getQuantity() - qty);
		Inventory updatedInv = inventoryRepository.save(inv);

//		check for ALERT after selling 
		if (updatedInv.getQuantity() <= updatedInv.getProduct().getLow_stock_threshold()) {
			triggerLowStockAlert(updatedInv);
		}

		return updatedInv;

	}

//	SELL product (reduction)
	@Transactional
	public Inventory sellProduct(UUID productId, Integer warehouseId, Integer quantityToSell) {

		Inventory updatedInv = subtractStock(productId, warehouseId, quantityToSell);

		recordTransaction(updatedInv, quantityToSell, Type.OUTBOUND,
				"Customer bought " + quantityToSell + " units of " + updatedInv.getProduct().getName());

		return updatedInv;

	}

//	TRANSFER product
	@Transactional
	public void transferProduct(UUID productId, Integer fromWhId, Integer toWhId, Integer qty) {

		Inventory sourceInv = subtractStock(productId, fromWhId, qty);
		recordTransaction(sourceInv, -qty, Type.TRANSFER, "Transfer out to Warehouse #" + toWhId);

		Inventory destInv = inventoryRepository.findByProduct_IdAndWareHouse_Id(productId, toWhId);

		if (destInv == null) {
			new Inventory();
		}

		destInv.setQuantity((destInv.getQuantity() == null ? 0 : destInv.getQuantity()) + qty);

		inventoryRepository.save(destInv);
		recordTransaction(destInv, qty, Type.TRANSFER, "Transfer in from Warehouse #" + fromWhId);

	}

	@Transactional
	public Inventory saveInventory(Inventory inventory) {

// Get the REAL Product from DB using the ID provided in JSON
		Product existingProduct = productRepository.findById(inventory.getProduct().getId())
				.orElseThrow(() -> new RuntimeException("Product not found"));

// Get the REAL Warehouse from DB
		WareHouse existingWh = warehouseRepository.findById(inventory.getWareHouse().getId())
				.orElseThrow(() -> new RuntimeException("Warehouse not found"));
		
		Inventory existingInv = inventoryRepository.findByProduct_IdAndWareHouse_Id(existingProduct.getId(), existingWh.getId());

		Inventory inventoryToSave;

		if (existingInv != null) {

// UPDATE - Get existing record and change quantity
			inventoryToSave = existingInv;
			inventoryToSave.setQuantity(inventory.getQuantity() + inventoryToSave.getQuantity()); 
			
		} else {

// NEW - Link product/warehouse and save as new record
			inventoryToSave = new Inventory();
			inventoryToSave.setProduct(existingProduct);
			inventoryToSave.setWareHouse(existingWh);
			inventoryToSave.setQuantity(inventory.getQuantity());
		}

// Now Hibernate knows these aren't "new" or "transient"
		Inventory saved = inventoryRepository.save(inventoryToSave);

		if (saved.getQuantity() <= existingProduct.getLow_stock_threshold()) {
			triggerLowStockAlert(saved);
		}

		return saved;
	}

	public InventoryDTO updateInventory(@Valid Inventory inventory) {

		Inventory existingInv = inventoryRepository
				.findByProduct_IdAndWareHouse_Id(inventory.getProduct().getId(), inventory.getWareHouse().getId());
				
		if(existingInv == null) {
			throw new RuntimeException("Inventory not found for this product/warehouse");
		}
		
		if (!existingInv.getVersion().equals(inventory.getVersion())) {
	        throw new OptimisticLockingFailureException("Version mismatch! Please refresh.");
	    }

		existingInv.setQuantity(inventory.getQuantity()); 
	
		inventoryRepository.save(existingInv);
		
		recordTransaction(existingInv, existingInv.getQuantity(), Type.INBOUND, 
				inventory.getQuantity() + " " + inventory.getProduct().getName() +
				" added in Warehouse #" + inventory.getWareHouse().getName());
		
		return convertToDTO(existingInv);
	}

	private void triggerLowStockAlert(Inventory inv) {

		Notification alert = new Notification();

		alert.setProduct(inv.getProduct());
		alert.setWarehouse(inv.getWareHouse());

		alert.setMessage("LOW STOCK ALERT: " + inv.getProduct().getName() + " in " + inv.getWareHouse().getName()
				+ " is down to " + inv.getQuantity());

		alert.setStatus(0); // 0 = UNREAD/SENT

		notificationRepo.save(alert);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("inventory-system@yourcompany.com");
		message.setTo("wahouse-manager@example.com");
		message.setSubject("Stock Pulse");
		message.setText("LOW STOCK: " + inv.getProduct().getName() + "\n" + "Alert! Only " + inv.getQuantity() + " items left in " + inv.getWareHouse().getName());

		mailSender.send(message);
	}

//	public void sendLowStockAlert(Inventory inv) {	
//		System.out.println("ALERT: " + inv.getProduct().getName() +
//							" is low in " + inv.getWareHouse().getName() +
//							" (Only " + inv.getQuantity() + " left!)");
//	}

//	public void sendEmailAlert(Inventory inv) {
//		
//		String subject = "URGENT: Low Stock for " + inv.getProduct().getName();
//		String body = "Product " + inv.getProduct().getName() + 
//						" is low in " + inv.getWareHouse().getName() + 
//							". Only " + inv.getQuantity() + " left.";
//		
//		System.out.println("--- MOCK EMAIL SENT ---");
//	    System.out.println("Subject: " + subject);
//	    System.out.println("Body: " + body);
//	    System.out.println("-----------------------");	
//	}

	public void recordTransaction(Inventory inv, Integer qty, Type type, String reason) {

// --- AUDIT LOG TRANSACTION - PART

		Transaction record = new Transaction();

		record.setInventory(inv);
		record.setQtyChange(qty);
		record.setReason(reason);
		record.setType(type);
		
		String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		record.setReason(reason + " on " + dateStr);

		record.setCreatedAt(LocalDateTime.now());

		transactionRepository.save(record);

// ---

	}
	
	public InventoryDTO convertToDTO(Inventory inventory) {
		
		InventoryDTO dto = new InventoryDTO();
		
		dto.setId(inventory.getId());
		dto.setQty(inventory.getQuantity());
		dto.setLastUpdated(inventory.getLastUpdated());
		dto.setProductName(inventory.getProduct().getName());
		dto.setWareHouseName(inventory.getWareHouse().getName());
		
		return dto;
	}

}
