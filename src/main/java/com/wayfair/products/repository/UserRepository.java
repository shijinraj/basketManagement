package com.wayfair.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wayfair.products.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
