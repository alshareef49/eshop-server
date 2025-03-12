package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}