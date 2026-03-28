package com.ownProject.GINS.jpa;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.product.Product;

import jakarta.transaction.Transactional;

@Transactional
public interface ProductRepository extends JpaRepository<Product, UUID> {

//	boolean deleteById(UUID id);

	Optional<Product> findById(UUID id);

}
