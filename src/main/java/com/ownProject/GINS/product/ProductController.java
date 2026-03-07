package com.ownProject.GINS.product;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ownProject.GINS.jpa.ProductRepository;

//import jakarta.validation.Valid;

@RestController
public class ProductController {

	private ProductRepository productRepository;
	private ProductDaoService productdaoService;
	
	public ProductController(ProductRepository productRepository, ProductDaoService productdaoService) {
		super();
		this.productRepository = productRepository;
		this.productdaoService = productdaoService;
	}

	@GetMapping("/products")
	public List<Product> getItems() {
		return productRepository.findAll();
//		return productdaoService.getAllItems();
	}
	
	@PostMapping("/products")
	public  ResponseEntity<Object> addItem(@RequestBody Product product) {
		
		Product savedProduct = productRepository.save(product);
		
		URI Location = ServletUriComponentsBuilder.fromCurrentRequest()
										.path("/{id}")
										.buildAndExpand(savedProduct.getId())
										.toUri();
		return ResponseEntity.created(Location).build();
	}
		
}
