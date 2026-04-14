package com.ownProject.GINS.inventory;

import java.time.LocalDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.UpdateTimestamp;

import com.ownProject.GINS.product.Product;
import com.ownProject.GINS.wareHouse.WareHouse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Valid
	@ManyToOne
	@NotNull(message = "Product is required")
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Valid
	@ManyToOne
	@NotNull(message = "Warehouse is required")
	@JoinColumn(name = "warehouse_id")
	private WareHouse wareHouse;
	
	@NotNull(message = "quantity is required")
	@Min(0)
	private Integer quantity;
	
//	@CreationTimestamp
	@UpdateTimestamp
	@TimeZoneStorage(TimeZoneStorageType.AUTO)
	private LocalDateTime lastUpdated;
	
	@Version
	private Integer version;
	
	public Inventory() {
		super();
	}

	public Inventory(Integer id, Product product, WareHouse wareHouse, Integer quantity, LocalDateTime lastUpdated, Integer version) {
		super();
		this.id = id;
		this.product = product;
		this.wareHouse = wareHouse;
		this.quantity = quantity;
		this.lastUpdated = lastUpdated;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

	public WareHouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WareHouse wareHouse) {
		this.wareHouse = wareHouse;
	}
	
	@Override
	public String toString() {
		return "Inventory [id=" + id + ", product=" + product + ", wareHouse=" + wareHouse + ", quantity=" + quantity
				+ ", lastUpdated=" + lastUpdated + ", version=" + version + "]";
	}
	
}
