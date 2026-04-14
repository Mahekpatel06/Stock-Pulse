package com.ownProject.GINS.dto;

import java.time.LocalDateTime;

public class InventoryDTO {

	private Integer id;
	private Integer qty;
	private String productName;
	private String wareHouseName;
	private LocalDateTime lastUpdated;
	
	public InventoryDTO() {
		super();
	}
	
	public InventoryDTO(Integer id, Integer qty, String productName, String wareHouseName, LocalDateTime lastUpdated) {
		super();
		this.id = id;
		this.qty = qty;
		this.productName = productName;
		this.wareHouseName = wareHouseName;
		this.lastUpdated = lastUpdated;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	@Override
	public String toString() {
		return "InventoryDTO [id=" + id + ", qty=" + qty + ", lastUpdated=" + lastUpdated + ", productName="
				+ productName + ", wareHouseName=" + wareHouseName + "]";
	}
	
}
