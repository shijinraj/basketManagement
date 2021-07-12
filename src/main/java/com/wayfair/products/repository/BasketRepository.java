package com.wayfair.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wayfair.products.domain.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long>{

}
