package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.models.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}