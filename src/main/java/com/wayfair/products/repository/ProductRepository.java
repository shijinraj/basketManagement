package com.wayfair.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wayfair.products.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
