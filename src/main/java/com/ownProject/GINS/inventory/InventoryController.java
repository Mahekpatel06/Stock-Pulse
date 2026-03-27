package com.ownProject.GINS.inventory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ownProject.GINS.dto.ProductStockDTO;
import com.ownProject.GINS.dto.WarehouseStockDTO;
import com.ownProject.GINS.jpa.InventoryRepository;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private InventoryRepository inventoryRepository;
	
	public InventoryController(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
	
	@GetMapping
	public List<Inventory> getInv() {
		return inventoryRepository.findAll();
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
	
	@PostMapping
	public  ResponseEntity<Inventory> addStock(@RequestBody Inventory inventory) {
		
		Inventory savedInventory = inventoryRepository.save(inventory);
		
//		return ResponseEntity.ok(savedInventory);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	            .path("/{id}")
	            .buildAndExpand(savedInventory.getId())
	            .toUri();
	            
//	    return ResponseEntity.created(location).body(savedInventory);
	    return ResponseEntity.created(location).build();
	}
	
	
}
