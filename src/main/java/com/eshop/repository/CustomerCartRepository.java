package com.eshop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.eshop.models.CustomerCart;

public interface CustomerCartRepository extends CrudRepository<CustomerCart, Integer> {
	Optional<CustomerCart> findByCustomerEmailId(String customerEmailId);
}