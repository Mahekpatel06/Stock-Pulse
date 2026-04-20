package com.ownProject.GINS.dto;

import java.util.UUID;

public class InventoryDTO {

	public Integer qty;
	public UUID productId;
	public Integer wareHouseId;
	public Integer version;
	
	public InventoryDTO() {
		super();
	}
	
	public InventoryDTO(Integer qty, UUID productId, Integer wareHouseId, Integer version) {
		super();
		this.qty = qty;
		this.productId = productId;
		this.wareHouseId = wareHouseId;
		this.version = version;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID uuid) {
		this.productId = uuid;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "InventoryDTO [qty=" + qty + ", productId=" + productId + ", wareHouseId=" + wareHouseId + ", version="
				+ version + "]";
	}
	
}
