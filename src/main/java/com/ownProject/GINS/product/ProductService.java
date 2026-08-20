package com.ownProject.GINS.product;

import com.ownProject.GINS.dto.ProductDTO;
import com.ownProject.GINS.jpa.ProductRepository;
import com.ownProject.GINS.exception.customExpClasses.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findallItems() {
        return productRepository.findAll();
    }

    public Product addProduct(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setLow_stock_threshold(productDto.getLow_stock_threshold());

        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProWithId(UUID id) {
        log.info("Fetching product from Database for ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product doesn't exist with id: " + id));
    }

    @CachePut(value = "products", key = "#id")  // makes sure that cache is updated whenever any type of data changes
    public Product updateProd(UUID id, ProductDTO productDto) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDto.getName());
            existingProduct.setPrice(productDto.getPrice());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @CacheEvict(value = "products", key = "#id")  // deletes the data from cache if user delete it
    public void deleteProd(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
