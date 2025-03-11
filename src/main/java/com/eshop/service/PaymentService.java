package com.eshop.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.eshop.dto.CardDTO;
import com.eshop.dto.TransactionDTO;
import com.eshop.exception.EShopException;

public interface PaymentService {

	Integer addCustomerCard(String customerEmailId, CardDTO cardDTO) throws EShopException, NoSuchAlgorithmException;

	void updateCustomerCard(CardDTO cardDTO) throws EShopException, NoSuchAlgorithmException;

	void deleteCustomerCard(String customerEmailId, Integer cardId) throws EShopException;

	CardDTO getCard(Integer cardId) throws EShopException;

	List<CardDTO> getCustomerCardOfCardType(String customerEmailId, String cardType) throws EShopException;

	Integer addTransaction(TransactionDTO transactionDTO) throws EShopException;

	TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
			throws EShopException, NoSuchAlgorithmException;

	List<CardDTO> getCardsOfCustomer(String customerEmailId) throws EShopException;

}