package com.ownProject.GINS.product;

import com.ownProject.GINS.dto.ProductDTO;
import com.ownProject.GINS.jpa.ProductRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {

    private ProductRepository productRepository;


    public List<Product> findallItems() {
        return productRepository.findAll();
    }

    public ResponseEntity<Object> addProduct(ProductDTO productDto) {
        Product product = new Product();

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setLow_stock_threshold(productDto.getLow_stock_threshold());

        Product savedProduct = productRepository.save(product);

        URI Location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getId()).toUri();
        return ResponseEntity.created(Location).build();
    }

    public EntityModel<Product> getProWithId(UUID id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new NoSuchElementException("Product doesn't exist with id:" + id);
        }

        EntityModel<Product> entityModel = EntityModel.of(product.get());
//		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllItems());
//		entityModel.add(link.withRel("all-products"));
        entityModel.add(linkTo(methodOn(this.getClass()).findallItems()).withRel("all-products"));
        entityModel.add(linkTo(methodOn(this.getClass()).getProWithId(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(this.getClass()).updateProd(id, null)).withRel("update"));
        entityModel.add(linkTo(methodOn(this.getClass()).deleteProd(id)).withRel("delete"));

        return entityModel;
    }

    public ResponseEntity<Product> updateProd(UUID id, ProductDTO productDto) {
        Product updatedProduct = productRepository.findById(id).map(existingProduct -> {

            existingProduct.setName(productDto.getName());
            existingProduct.setPrice(productDto.getPrice());

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

        return ResponseEntity.ok(updatedProduct); // Wrap in ResponseEntity
    }

    public ResponseEntity<Void> deleteProd(UUID id) {

        productRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
}
