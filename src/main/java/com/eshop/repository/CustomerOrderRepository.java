package com.eshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eshop.models.Order;

public interface CustomerOrderRepository extends CrudRepository<Order, Integer> {
	List<Order> findByCustomerEmailId(String customerEmailId);
}