package com.ownProject.GINS.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

	@NotBlank(message = "Product name is required")
	public String name;
	
	@NotNull(message = "Product price is required")
	@Min(value = 0, message = "Price must be positive")
	public Double price;
	
	@NotBlank(message = "Product category is required")
	public String category;

	@NotNull(message = "Low stock threshold is required")
	@Min(value = 0, message = "Threshold must be positive")
	public Integer low_stock_threshold;

	public ProductDTO() {
		super();
	}

	public ProductDTO(String name, Double price, String category, Integer low_stock_threshold) {
		super();
		this.name = name;
		this.price = price;
		this.category = category;
		this.low_stock_threshold = low_stock_threshold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "ProductDTO [name=" + name + ", price=" + price + ", category=" + category + ", low_stock_threshold="
				+ low_stock_threshold + "]";
	}   
	
}
