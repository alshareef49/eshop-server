package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}