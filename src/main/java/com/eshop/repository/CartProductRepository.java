package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.entity.CartProduct;

public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

}