package com.ownProject.GINS.specification;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ownProject.GINS.inventory.Inventory;

import jakarta.persistence.criteria.Predicate;

public class InventorySpec {
	
	public static Specification<Inventory> withDynamicQuery(String category, String location) {
		
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if(category != null && !category.isEmpty()) {
				predicates.add((Predicate) criteriaBuilder.equal(root.get("product").get("category"), category));
			}
			
			if(location != null && !location.isEmpty()) {
				predicates.add((Predicate) criteriaBuilder.equal(root.get("wareHouse").get("locationCity"), location));
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
