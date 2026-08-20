package com.ownProject.GINS.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
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

import com.ownProject.GINS.dto.ProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Product APIs")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	@Operation(summary = "get all products")
	public List<Product> getAllItems() {
		return productService.findallItems();
	}

	@PostMapping("/products")
	@Operation(summary = "add new product")
	public ResponseEntity<Object> addItem(@RequestBody ProductDTO productDto) {
		Product savedProduct = productService.addProduct(productDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedProduct.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/products/{id}")
	@Operation(summary = "get product by its ID")
	public EntityModel<Product> getItem(@PathVariable UUID id) {
		Product product = productService.getProWithId(id);
		
		EntityModel<Product> entityModel = EntityModel.of(product);
		entityModel.add(linkTo(methodOn(ProductController.class).getAllItems()).withRel("all-products"));
		entityModel.add(linkTo(methodOn(ProductController.class).getItem(id)).withSelfRel());
		entityModel.add(linkTo(methodOn(ProductController.class).updateItem(id, null)).withRel("update"));
		entityModel.add(linkTo(methodOn(ProductController.class).deleteItem(id)).withRel("delete"));

		return entityModel;
	}

	@PutMapping("/products/{id}")
	@Operation(summary = "change the price of product")
	public ResponseEntity<Product> updateItem(@PathVariable UUID id, @RequestBody ProductDTO productDto) {
		Product updatedProduct = productService.updateProd(id, productDto);
		return ResponseEntity.ok(updatedProduct);
	}

	@DeleteMapping("/products/{id}")
	@Operation(summary = "to destroy the product(if in-case)")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") UUID id) {
		productService.deleteProd(id);
		return ResponseEntity.noContent().build();
	}
}
