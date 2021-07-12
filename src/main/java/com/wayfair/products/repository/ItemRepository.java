package com.wayfair.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wayfair.products.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
