package com.ownProject.GINS.notification;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.ownProject.GINS.product.Product;
import com.ownProject.GINS.wareHouse.WareHouse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Notification {

	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "warehouse_id")
	private WareHouse warehouse;
	
	@NotNull
	private String message;
	
	private Integer status;		// 	e.g., SENT(0), READ(1)	The delivery status of the notification
	
	@CreationTimestamp
	@TimeZoneStorage(TimeZoneStorageType.AUTO)
	private LocalDateTime createdAt;

	public Notification() {
		super();
	}
	
	public Notification(Integer id, Product product, WareHouse warehouse, @NotNull String message, Integer status,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.product = product;
		this.warehouse = warehouse;
		this.message = message;
		this.status = status;
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public WareHouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WareHouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", product=" + product + ", warehouse=" + warehouse + ", message=" + message
				+ ", status=" + status + ", createdAt=" + createdAt + "]";
	}
	
	
}
