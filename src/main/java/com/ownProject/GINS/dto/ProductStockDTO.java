package com.ownProject.GINS.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductStockDTO {

	public UUID productId;
	public String productName;
	public List<Map<String, Object>> locations;	// simple way to store locations

	public ProductStockDTO() {
		super();
	}
	
	public ProductStockDTO(UUID productId, String productName, List<Map<String, Object>> locations) {
		super();
		this.setProductId(productId);
		this.setProductName(productName);
		this.setLocations(locations);
	}
	
	public UUID getProductId() {
		return productId;
	}
	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<Map<String, Object>> getLocations() {
		return locations;
	}
	public void setLocations(List<Map<String, Object>> locations) {
		this.locations = locations;
	}
	
	
}
