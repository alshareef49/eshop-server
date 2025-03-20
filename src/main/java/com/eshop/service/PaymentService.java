package com.eshop.service;
import com.eshop.dto.TransactionDTO;
import com.eshop.exception.EShopException;

public interface PaymentService {

	Integer addTransaction(TransactionDTO transactionDTO) throws EShopException;
}