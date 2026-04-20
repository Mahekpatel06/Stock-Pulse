package com.ownProject.GINS.dto;

import jakarta.validation.constraints.Min;

public class ProductDTO {

	public String name;
	
	@Min(0)
	public Double price;
	
	public String category;
	public Integer low_stock_threshold;

	public ProductDTO(String name, @Min(0) Double price, String category, Integer low_stock_threshold) {
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
