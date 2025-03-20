package com.eshop.service;

import com.eshop.dto.CustomerDTO;
import com.eshop.exception.EShopException;
import com.eshop.models.Customer;

import java.util.List;

public interface CustomerService {

	CustomerDTO authenticateCustomer(String emailId, String password) throws EShopException;

	String registerNewCustomer(CustomerDTO customerDTO) throws EShopException;

	void updateShippingAddress(String customerEmailId, String address) throws EShopException;

	void deleteShippingAddress(String customerEmailId) throws EShopException;

	CustomerDTO getCustomerByEmailId(String emailId) throws EShopException;

	void updatePassword(String customerEmailId, String newPassword) throws EShopException;

	void updatePhoneNumber(String customerEmailId, String phoneNumber) throws EShopException;
	void saveAdmin(CustomerDTO customer) throws EShopException;

	List<Customer> getAllCustomers() throws EShopException;
}