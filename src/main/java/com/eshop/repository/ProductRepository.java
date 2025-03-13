package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.models.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}