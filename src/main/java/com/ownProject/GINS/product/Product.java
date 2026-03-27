package com.ownProject.GINS.product;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String name;
	
	@Min(0)
	private Integer quantity;
	
	@Min(0)
	private Double price;
	
	private String category;
	
	private Integer low_stock_threshold;   // the no. that triggers a notification
	
	public Product() {
		super();
	}
	
	public Product(UUID id, String name, @Min(0) Integer quantity, @Min(0) Double price, String category,
			Integer low_stock_threshold) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.category = category;
		this.low_stock_threshold = low_stock_threshold;
	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Integer getLow_stock_threshold() {
		return low_stock_threshold;
	}
	
	public void setLow_stock_threshold(Integer low_stock_threshold) {
		this.low_stock_threshold = low_stock_threshold;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", category="
				+ category + ", low_stock_threshold=" + low_stock_threshold + "]";
	}
	
}
