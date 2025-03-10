package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

	// add methods if required

}