package com.ownProject.GINS.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ownProject.GINS.jpa.ProductRepository;

//import jakarta.validation.Valid;

@RestController
public class ProductController {

	private ProductRepository productRepository;

	public ProductController(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	@GetMapping("/products")
	public List<Product> getAllItems() {
		return productRepository.findAll();
	}

	@PostMapping("/products")
	public ResponseEntity<Object> addItem(@RequestBody Product product) {

		Product savedProduct = productRepository.save(product);

		URI Location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedProduct.getId()).toUri();
		return ResponseEntity.created(Location).build();
	}

	@GetMapping("/products/{id}")
	public EntityModel<Product> getItem(@PathVariable UUID id) {
		Optional<Product> product = productRepository.findById(id);

		if (product.isEmpty()) {
			throw new NoSuchElementException("Product doesn't exist with id:" + id);
		}
		
		EntityModel<Product> entityModel = EntityModel.of(product.get());
//		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllItems());
//		entityModel.add(link.withRel("all-products"));
		entityModel.add(linkTo(methodOn(this.getClass()).getAllItems()).withRel("all-products"));
		entityModel.add(linkTo(methodOn(this.getClass()).getItem(id)).withSelfRel());
		entityModel.add(linkTo(methodOn(this.getClass()).updateItem(id, null)).withRel("update"));
		entityModel.add(linkTo(methodOn(this.getClass()).deleteItem(id)).withRel("delete"));
		return entityModel;
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateItem(@PathVariable UUID id, @RequestBody Product product) {

		Product updatedProduct = productRepository.findById(id).map(existingProduct -> {

			existingProduct.setName(product.getName());
			existingProduct.setQuantity(product.getQuantity());
			existingProduct.setPrice(product.getPrice());

			return productRepository.save(existingProduct);
		}).orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
		
		return ResponseEntity.ok(updatedProduct); // Wrap in ResponseEntity
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") UUID id) {
		productRepository.deleteById(id);
		
//		if(deleted) 
//			ResponseEntity.ok("Resource with ID " + id + "deleted successfully.");

	    return ResponseEntity.noContent().build(); // Returns 204 No Content
	}
}
