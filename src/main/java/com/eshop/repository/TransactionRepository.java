package com.eshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.eshop.models.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}