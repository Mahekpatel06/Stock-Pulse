package com.ownProject.GINS.dto;

import java.util.List;
import java.util.Map;

public class WarehouseStockDTO {

	public Integer warehouseId;
	public String location;
	public List<Map<String, Object>> invetoryItems;	// Simple way to store items
	
	
	public WarehouseStockDTO() {
		super();
	}
	
	public WarehouseStockDTO(Integer warehouseId, String location, List<Map<String, Object>> invetoryItems) {
		super();
		this.warehouseId = warehouseId;
		this.location = location;
		this.invetoryItems = invetoryItems;
	}
	public Integer getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Map<String, Object>> getInvetoryItems() {
		return invetoryItems;
	}
	public void setInvetoryItems(List<Map<String, Object>> invetoryItems) {
		this.invetoryItems = invetoryItems;
	}
	
}
