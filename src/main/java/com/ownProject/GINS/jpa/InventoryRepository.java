package com.ownProject.GINS.jpa;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.inventory.Inventory;

import jakarta.transaction.Transactional;

@Transactional
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	
	// Finds all inventory rows for a specific product UUID
	List<Inventory> findByProduct_Id(UUID productId);
	
	// Finds all inventory rows for a specific wareohuse ID
	List<Inventory> findByWareHouse_Id(Integer warehouseId);

	Inventory findByProduct_IdAndWareHouse_Id(UUID productId, Integer warehouseId);
	
}
