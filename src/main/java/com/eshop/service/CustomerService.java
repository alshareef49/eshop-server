package com.eshop.service;

import com.eshop.dto.CustomerDTO;
import com.eshop.exception.EShopException;

public interface CustomerService {

	CustomerDTO authenticateCustomer(String emailId, String password) throws EShopException;

	String registerNewCustomer(CustomerDTO customerDTO) throws EShopException;

	void updateShippingAddress(String customerEmailId, String address) throws EShopException;

	void deleteShippingAddress(String customerEmailId) throws EShopException;

	CustomerDTO getCustomerByEmailId(String emailId) throws EShopException;

}