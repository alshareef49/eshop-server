package com.eshop.service;

import java.util.Optional;

import com.eshop.dto.CustomerDTO;
import com.eshop.models.Customer;
import com.eshop.exception.EShopException;
import com.eshop.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public CustomerDTO authenticateCustomer(String emailId, String password) throws EShopException {
		CustomerDTO customerDTO = null;

		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EShopException("CustomerService.CUSTOMER_NOT_FOUND"));
		if (!password.equals(customer.getPassword()))
			throw new EShopException("CustomerService.INVALID_CREDENTIALS");

		customerDTO = new CustomerDTO();
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setPassword(customer.getPassword());
		customerDTO.setNewPassword(customer.getPassword());
		customerDTO.setAddress(customer.getAddress());
		return customerDTO;
	}

	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws EShopException {
		String registeredWithEmailId = null;
		boolean isEmailNotAvailable = customerRepository.findById(customerDTO.getEmailId().toLowerCase()).isEmpty();
		boolean isPhoneNumberNotAvailable = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber())
				.isEmpty();
		if (isEmailNotAvailable) {
			if (isPhoneNumberNotAvailable) {
				Customer customer = new Customer();
				customer.setEmailId(customerDTO.getEmailId().toLowerCase());
				customer.setName(customerDTO.getName());
				customer.setPassword((new BCryptPasswordEncoder()).encode(customerDTO.getPassword()));
				customer.setPhoneNumber(customerDTO.getPhoneNumber());
				customer.setAddress(customerDTO.getAddress());
				customerRepository.save(customer);
				registeredWithEmailId = customer.getEmailId();
			} else {
				throw new EShopException("CustomerService.PHONE_NUMBER_ALREADY_IN_USE");
			}
		} else {
			throw new EShopException("CustomerService.EMAIL_ID_ALREADY_IN_USE");
		}
		return registeredWithEmailId;

	}

	@Override
	public void updateShippingAddress(String customerEmailId, String address) throws EShopException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EShopException("CustomerService.CUSTOMER_NOT_FOUND"));
		customer.setAddress(address);
	}

	@Override
	public void deleteShippingAddress(String customerEmailId) throws EShopException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EShopException("CustomerService.CUSTOMER_NOT_FOUND"));
		customer.setAddress(null);
	}

	
	@Override
	public CustomerDTO getCustomerByEmailId(String emailId) throws EShopException {
		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EShopException("CustomerService.CUSTOMER_NOT_FOUND"));
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setAddress(customer.getAddress());
		return customerDTO;
	}

}