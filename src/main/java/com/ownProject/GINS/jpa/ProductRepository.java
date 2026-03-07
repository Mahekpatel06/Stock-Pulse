package com.ownProject.GINS.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
