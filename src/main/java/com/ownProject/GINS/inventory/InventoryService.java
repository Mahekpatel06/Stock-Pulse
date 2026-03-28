package com.ownProject.GINS.inventory;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ownProject.GINS.jpa.InventoryRepository;
import com.ownProject.GINS.jpa.NotificationRepository;
import com.ownProject.GINS.jpa.ProductRepository;
import com.ownProject.GINS.jpa.WareHouseRepository;
import com.ownProject.GINS.notification.Notification;
import com.ownProject.GINS.product.Product;
import com.ownProject.GINS.wareHouse.WareHouse;

@Service
public class InventoryService {

	@Autowired
	private NotificationRepository notificationRepo;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private WareHouseRepository warehouseRepository;
	
//	SELL product (reduction)
	@Transactional 
	public Inventory sellProduct(UUID productId, Integer warehouseId, Integer quantityToSell) {
		
		Inventory inv = inventoryRepository.findByProduct_IdAndWareHouse_Id(productId, warehouseId);
		
		if(inv == null) {
			throw new RuntimeException("Product not found in this warehouse");
		}
		
		if(inv.getQuantity() < quantityToSell ) {
			throw new RuntimeException("Insufficient stock! Only " + inv.getQuantity() + " available.");
		}
		
		inv.setQuantity(inv.getQuantity() - quantityToSell);
		Inventory updatedInv = inventoryRepository.save(inv);
		
//		check for ALERT after selling 
		if(updatedInv.getQuantity() <= updatedInv.getProduct().getLow_stock_threshold()) {
			triggerLowStockAlert(updatedInv);
		}
		
		return updatedInv;
	}
	
//	TRANSFER product
	@Transactional
	public void transferProduct(UUID productId, Integer fromWhId, Integer toWhId, Integer qty) {
		
		sellProduct(productId, fromWhId, qty);
		
		Inventory destInv = inventoryRepository.findByProduct_IdAndWareHouse_Id(productId, toWhId);
		
		if(destInv == null) {
			new Inventory();
		}
		
		destInv.setQuantity((destInv.getQuantity() == null ? 0 : destInv.getQuantity()) + qty);
		
		inventoryRepository.save(destInv);
	}
	
	public Inventory saveInventory(Inventory inventory) {
		
        // 1. Get the REAL Product from DB using the ID provided in JSON
        Product existingProduct = productRepository.findById(inventory.getProduct().getId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Get the REAL Warehouse from DB
        WareHouse existingWh = warehouseRepository.findById(inventory.getWareHouse().getId())
            .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        // 3. Attach the REAL objects to your inventory
        inventory.setProduct(existingProduct);
        inventory.setWareHouse(existingWh);

        // 4. Now Hibernate knows these aren't "new" or "transient"
        Inventory saved = inventoryRepository.save(inventory);
        
        if(saved.getQuantity() <= existingProduct.getLow_stock_threshold()) {
        	triggerLowStockAlert(saved);
        }
        
        return saved;
    }

	private void triggerLowStockAlert(Inventory inv) {
		
	    Notification alert = new Notification();
	    
	    alert.setProduct(inv.getProduct());
	    alert.setWarehouse(inv.getWareHouse());
	    
	    alert.setMessage("LOW STOCK ALERT: " + inv.getProduct().getName() + 
	                     " in " + inv.getWareHouse().getName() + 
	                     " is down to " + inv.getQuantity());
	    
	    alert.setStatus(0); // 0 = UNREAD/SENT
	    
	    notificationRepo.save(alert);
	}
	
//	public void sendLowStockAlert(Inventory inv) {	
//		System.out.println("ALERT: " + inv.getProduct().getName() +
//							" is low in " + inv.getWareHouse().getName() +
//							" (Only " + inv.getQuantity() + " left!)");
//	}
}
