package com.ownProject.GINS.inventory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ownProject.GINS.dto.InventoryDTO;
import com.ownProject.GINS.dto.ProductStockDTO;
import com.ownProject.GINS.dto.WarehouseStockDTO;
import com.ownProject.GINS.jpa.InventoryRepository;
import com.ownProject.GINS.specification.InventorySpec;
import com.ownProject.GINS.transaction.Transaction.Type;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private InventoryRepository inventoryRepository;
	private InventoryService inventoryService;
	
	public InventoryController(InventoryRepository inventoryRepository, InventoryService inventoryService) {
		this.inventoryRepository = inventoryRepository;
		this.inventoryService = inventoryService;
	}
	
	@GetMapping
	public List<Inventory> getInv() {
		return inventoryRepository.findAll();
	}
	
	@GetMapping("/search")
	public Page<InventoryDTO> searchInventory(@RequestParam(required = false) String category,
										   @RequestParam(required = false) String warehouseLoc,
										   @RequestParam(defaultValue = "0") int page,
										   @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		
		Specification<Inventory> spec = InventorySpec.withDynamicQuery(category, warehouseLoc);
		
		return inventoryRepository.findAll(spec, pageable).map(this::convertToDTO);
	}
		
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductStockDTO> getSpecificProduct(@PathVariable UUID id) {
		
		List<Inventory> items = inventoryRepository.findByProduct_Id(id);
		
		if(items.isEmpty()) return ResponseEntity.notFound().build();
		
		ProductStockDTO response = new ProductStockDTO();
		response.productId = id;
		response.productName = items.get(0).getProduct().getName();
		
		response.locations = items.stream().map(inv -> {
			
			Map<String, Object> map = new HashMap<>();
			map.put("warehouseId", inv.getWareHouse().getId());
			map.put("warehouseName", inv.getWareHouse().getName());
			map.put("quantity", inv.getQuantity());
			return map;
		}).collect(Collectors.toList());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/warehouses/{id}")
	public ResponseEntity<WarehouseStockDTO> getSpecificWh(@PathVariable Integer id) {
		
		List<Inventory> items = inventoryRepository.findByWareHouse_Id(id);
		
		if(items.isEmpty()) return ResponseEntity.notFound().build();
		
		WarehouseStockDTO response = new WarehouseStockDTO();
		response.warehouseId = id;
		response.warehouseName = items.get(0).getWareHouse().getName();
		response.location = items.get(0).getWareHouse().getLocationCity();	// assuming location field exists
		
		response.invetoryItems = items.stream().map(inv -> {
			
			Map<String, Object> map = new HashMap<>();
			map.put("productId", inv.getProduct().getId());
			map.put("productName", inv.getProduct().getName());
			map.put("quantity", inv.getQuantity());
			return map;
		}).collect(Collectors.toList());
				
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Inventory> addStock(@Valid @RequestBody Inventory inventory) {
		
		Inventory savedInventory = inventoryService.saveInventory(inventory);
		
//		return ResponseEntity.ok(savedInventory);   // 200 ok state
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	            .path("/{id}")
	            .buildAndExpand(savedInventory.getId())
	            .toUri();
	            
		inventoryService.recordTransaction(savedInventory, inventory.getQuantity(), Type.INBOUND, 
								inventory.getProduct().getName() + " newly added in Warehouse #" + inventory.getWareHouse().getName());
		
//	    return ResponseEntity.created(location).body(savedInventory);
	    return ResponseEntity.created(location).build();	// 201 created state
	}
		
	@PutMapping("/changeQty") 
	public ResponseEntity<InventoryDTO> changeQty(@Valid @RequestBody Inventory inventory) {
		
		inventoryService.updateInventory(inventory);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/sell")
	public ResponseEntity<Inventory> sellproduct(@RequestParam UUID productId,
												 @RequestParam Integer warehouseId,
												 @RequestParam Integer qty) {
		
		Inventory sellingProduct = inventoryService.sellProduct(productId, warehouseId, qty);
		
		return ResponseEntity.ok(sellingProduct);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<String> transferProduct(@RequestParam UUID productId, 
												  @RequestParam Integer fromWhId, 
												  @RequestParam Integer toWhId,
												  @RequestParam Integer qty) {
		
		inventoryService.transferProduct(productId, fromWhId, toWhId, qty);
		
		return ResponseEntity.ok("Transfer Successful: Moved " + qty + " items.");
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
