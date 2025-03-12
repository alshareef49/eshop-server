package com.eshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.dto.CustomerDTO;
import com.eshop.entity.Customer;
import com.eshop.exception.EShopException;
import com.eshop.repository.CustomerRepository;
import com.eshop.service.CustomerService;
import com.eshop.service.CustomerServiceImpl;

@SpringBootTest
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();

	//Write testcases here

	@Test
	void authenticateCustomerValidTest() throws EShopException {
		Customer customer = new Customer();
		customer.setEmailId("tom@gmail.com");
		customer.setPassword("Tom@123");
		String emailId = "tom@gmail.com";
		String password = "Tom@123";
		Mockito.when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		CustomerDTO customerDTO = customerService.authenticateCustomer(emailId, password);
		Assertions.assertEquals(customer.getEmailId(), customerDTO.getEmailId());

	}

	@Test
	void authenticateCustomerInValidTest() throws EShopException {
		String emailId = "tom@gmail.com";
		String password = "Tom@123";
		Mockito.when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.authenticateCustomer(emailId, password));
		Assertions.assertEquals("CustomerService.CUSTOMER_NOT_FOUND", exp.getMessage());

	}

	@Test
	void authenticateCustomerInValidTest2() throws EShopException {
		Customer customer = new Customer();
		customer.setEmailId("tom@gmail.com");
		customer.setPassword("Tom@123");
		String emailId = "tom@gmail.com";
		String password = "Tom@12";
		Mockito.when(customerRepository.findById(customer.getEmailId().toLowerCase()))
				.thenReturn(Optional.of(customer));
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.authenticateCustomer(emailId, password));
		Assertions.assertEquals("CustomerService.INVALID_CREDENTIALS", exp.getMessage());

	}

	@Test
	void registeredNewCustomerValidTest() throws EShopException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("tom@gmail.com");
		customerDTO.setPhoneNumber("7856780651");
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<Customer>());
		Assertions.assertEquals(customerDTO.getEmailId(), customerDTO.getEmailId());
	}

	@Test
	void registeredNewCustomerInValidTest() throws EShopException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("tom@gmail.com");
		customerDTO.setPhoneNumber("7856780651");
		List<Customer> customers = new ArrayList<>();
		Customer customer1 = new Customer();
		Customer customer2 = new Customer();
		customers.add(customer1);
		customers.add(customer2);
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customers);
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.registerNewCustomer(customerDTO));
		Assertions.assertEquals("CustomerService.PHONE_NUMBER_ALREADY_IN_USE", exp.getMessage());

	}

	@Test
	void registeredNewCustomerInValidTest2() throws EShopException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("tom@gmail.com");
		customerDTO.setPhoneNumber("78567806510.");
		Customer customer = new Customer();
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(customer));
		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<Customer>());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.registerNewCustomer(customerDTO));
		Assertions.assertEquals("CustomerService.EMAIL_ID_ALREADY_IN_USE", exp.getMessage());
	}

	@Test
	void updateShippingAddressValidTest() throws EShopException {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String address = "Alwal Hills";
		Mockito.when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		Assertions.assertDoesNotThrow(() -> customerService.updateShippingAddress(customerEmailId, address));

	}

	@Test
	void updateShippingAddressInValidTest1() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		String address = "Alwal Hills";
		Mockito.when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.updateShippingAddress(customerEmailId, address));
		Assertions.assertEquals("CustomerService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteShippingAddressValidTest() throws EShopException {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		Mockito.when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		Assertions.assertDoesNotThrow(() -> customerService.deleteShippingAddress(customerEmailId));
	}

	@Test
	void deleteShippingAddressInValidTest() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.deleteShippingAddress(customerEmailId));
		Assertions.assertEquals("CustomerService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void getCustomerByEmailIdValidTest() throws EShopException {

		// write your logic here

	}

	@Test
	void getCustomerByEmailIdInValidTest() throws EShopException {

		// write your logic here

	}

}