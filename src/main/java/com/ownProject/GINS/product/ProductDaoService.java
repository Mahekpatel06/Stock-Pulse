package com.ownProject.GINS.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class ProductDaoService {
	
	private static List<Product> products = new ArrayList<Product>();

	public List<Product> getAllItems() {
		return products;
	}

	
	
}
