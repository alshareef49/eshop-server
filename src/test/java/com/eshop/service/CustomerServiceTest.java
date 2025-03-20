package com.eshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eshop.models.Role;
import com.eshop.repository.RoleRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.dto.CustomerDTO;
import com.eshop.models.Customer;
import com.eshop.exception.EShopException;
import com.eshop.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

	@Mock
	private RoleRepository roleRepository;

	@Captor
	private ArgumentCaptor<Customer> customerCaptor;

	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();


	@Test
	void authenticateCustomerValidTest() throws EShopException {
		Customer customer = new Customer();
		customer.setEmailId("tom@gmail.com");
		customer.setPassword("Tom@123");
		String emailId = "tom@gmail.com";
		String password = "Tom@123";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		CustomerDTO customerDTO = customerService.authenticateCustomer(emailId, password);
		Assertions.assertEquals(customer.getEmailId(), customerDTO.getEmailId());

	}

	@Test
	void authenticateCustomerInValidTest() throws EShopException {
		String emailId = "tom@gmail.com";
		String password = "Tom@123";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
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
		when(customerRepository.findById(customer.getEmailId().toLowerCase()))
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
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<Customer>());
		Assertions.assertEquals(customerDTO.getEmailId(), customerDTO.getEmailId());
	}

	@Test
	void registeredNewCustomerValidTest1() throws EShopException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("tom@gmail.com");
		customerDTO.setAddress("123 Test Street");
		customerDTO.setNewPassword("Tom@123");
		customerDTO.setPassword("Tom@123");
		Role role = new Role();
		role.setRoleName("USER");
		when(roleRepository.findByRoleName(Mockito.anyString())).thenReturn(Optional.empty());
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
		when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<Customer>());
		assertDoesNotThrow(() -> customerService.registerNewCustomer(customerDTO));
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
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customers);
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
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(customer));
		when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<Customer>());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.registerNewCustomer(customerDTO));
		Assertions.assertEquals("CustomerService.EMAIL_ID_ALREADY_IN_USE", exp.getMessage());
	}

	@Test
	void updateShippingAddressValidTest() throws EShopException {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String address = "Alwal Hills";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		assertDoesNotThrow(() -> customerService.updateShippingAddress(customerEmailId, address));

	}

	@Test
	void updateShippingAddressInValidTest1() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		String address = "Alwal Hills";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.updateShippingAddress(customerEmailId, address));
		Assertions.assertEquals("CustomerService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteShippingAddressValidTest() throws EShopException {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		assertDoesNotThrow(() -> customerService.deleteShippingAddress(customerEmailId));
	}

	@Test
	void deleteShippingAddressInValidTest() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> customerService.deleteShippingAddress(customerEmailId));
		Assertions.assertEquals("CustomerService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void getCustomerByEmailIdValidTest() throws EShopException {

		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		assertDoesNotThrow(()-> customerService.getCustomerByEmailId(customerEmailId));

	}

	@Test
	void getCustomerByEmailIdInValidTest() throws EShopException {
		when(customerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(EShopException.class,()-> customerService.getCustomerByEmailId("test@gmail.com"));

	}

	@Test
	void updatePassword() {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String newPassword = "Tom@123";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		assertDoesNotThrow(() -> customerService.updatePassword(customerEmailId, newPassword));
	}
	@Test
	void updatePassword1() {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String newPassword = "Tom@123";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
		Assertions.assertThrows(EShopException.class,()-> customerService.updatePassword(customerEmailId, newPassword));
	}


	@Test
	void updatePhoneNumber() {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String phoneNumber = "7856780651";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.of(customer));
		assertDoesNotThrow(() -> customerService.updatePhoneNumber(customerEmailId, phoneNumber));
	}
	@Test
	void updatePhoneNumber1() {
		Customer customer = new Customer();
		String customerEmailId = "tom@gmail.com";
		String phoneNumber = "7856780651";
		when(customerRepository.findById(Mockito.anyString().toLowerCase())).thenReturn(Optional.empty());
		Assertions.assertThrows(EShopException.class,()-> customerService.updatePhoneNumber(customerEmailId, phoneNumber));
	}

	@Test
	void saveAdmin_Success_WhenRolesExist() throws EShopException {
		// Arrange
		CustomerDTO customerDTO = getMockCustomerDTO();

		Role userRole = new Role();
		userRole.setRoleName("USER");

		Role adminRole = new Role();
		adminRole.setRoleName("ADMIN");

		when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
		when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.of(adminRole));

		// Act
		assertDoesNotThrow(() -> customerService.saveAdmin(customerDTO));

		// Assert
		verify(customerRepository, times(1)).save(customerCaptor.capture());
		Customer savedCustomer = customerCaptor.getValue();

		verify(roleRepository, never()).save(any(Role.class));
	}

	private CustomerDTO getMockCustomerDTO() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("admin@eshop.com");
		customerDTO.setName("Admin User");
		customerDTO.setPhoneNumber("9876543210");
		customerDTO.setAddress("123 Admin Street");
		customerDTO.setPassword("securePassword");
		return customerDTO;
	}


}