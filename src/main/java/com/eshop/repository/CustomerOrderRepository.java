package com.eshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eshop.entity.Order;

public interface CustomerOrderRepository extends CrudRepository<Order, Integer> {
	List<Order> findByCustomerEmailId(String customerEmailId);
}