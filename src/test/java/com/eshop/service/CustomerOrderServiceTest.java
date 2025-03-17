package com.eshop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eshop.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.models.Order;
import com.eshop.models.OrderedProduct;
import com.eshop.exception.EShopException;
import com.eshop.repository.CustomerOrderRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerOrderServiceTest {

	@Mock
	private CustomerOrderRepository orderRepository;
	@Mock
	private CustomerService customerService;

	@InjectMocks
	CustomerOrderService customerOrderService = new CustomerOrderServiceImpl();

	private OrderDTO orderDTO;
	private CustomerDTO customerDTO;

	@BeforeEach
	void setUp() {
		customerDTO = new CustomerDTO();
		customerDTO.setEmailId("test@example.com");
		customerDTO.setAddress("123 Test Street");

		orderDTO = new OrderDTO();
		orderDTO.setCustomerEmailId("test@example.com");
		orderDTO.setPaymentThrough("CREDIT_CARD");
		orderDTO.setOrderedProducts(new ArrayList<>());
	}


	@Test
	public void findOrderByCustomerEmailIdValidTest() throws EShopException {

		LocalDate date = LocalDate.of(2022, 02, 12);
		LocalTime time = LocalTime.of(16, 02);
		String emailId = "name@gmail.com";
		List<Order> orderList = new ArrayList<>();
		Order order = new Order();
		order.setOrderId(1234);
		order.setCustomerEmailId("emailId");
		order.setDateOfOrder(LocalDateTime.now());
		order.setDiscount(13.00d);
		order.setTotalPrice(34000d);
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentThrough(PaymentThrough.DEBIT_CARD);
		order.setDateOfDelivery(LocalDateTime.of(date, time));
		order.setDeliveryAddress("4th Line,Rome");
		List<OrderedProduct> orderedProductList = new ArrayList<>();
		OrderedProduct orderedProduct = new OrderedProduct();
		orderedProduct.setOrderedProductId(2345);
		orderedProduct.setProductId(3456);
		orderedProduct.setQuantity(4);
		orderedProductList.add(orderedProduct);
		order.setOrderedProducts(orderedProductList);
		orderList.add(order);
		Integer orderIdFromTest = null;
		Integer orderIdFromService = null;

		Mockito.when(orderRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(orderList);

		for (Order order3 : orderList) {
			orderIdFromTest = order3.getOrderId();

		}

		List<OrderDTO> orderDTOList = customerOrderService.findOrdersByCustomerEmailId(emailId);
		for (OrderDTO orderDTO : orderDTOList) {
			orderIdFromService = orderDTO.getOrderId();
			assertEquals(orderIdFromTest, orderIdFromService);
		}

	}
	
	@Test
	public void findOrderByCustomerEmailIdInValidTest() {
		String emailId = "name@gmail.com";

		Mockito.when(orderRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(new ArrayList<Order>());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.findOrdersByCustomerEmailId(emailId));
		assertEquals("OrderService.NO_ORDERS_FOUND", excep.getMessage());
	}

	@Test
	public void placeOrderInValidTest3() throws EShopException {

		OrderDTO orderDTO1 = new OrderDTO();
		orderDTO1.setOrderId(111);
		orderDTO1.setCustomerEmailId("name@gmail.com");
		orderDTO1.setPaymentThrough("DEBIT_CARD");

		List<OrderedProductDTO> productList = new ArrayList<>();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductId(1);
		productDTO.setDiscount(5.00d);
		productDTO.setAvailableQuantity(1);
		productDTO.setQuantity(12);
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setPrice(1000.00d);
		productDTO.setBrand("Brand");
		orderDTO1.setOrderedProducts(productList);

		OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
		orderedProductDTO.setOrderedProductId(1);
		orderedProductDTO.setQuantity(1);
		orderedProductDTO.setProduct(productDTO);
		productList.add(orderedProductDTO);

		Mockito.when(customerService.getCustomerByEmailId(Mockito.anyString())).thenReturn(customerDTO);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(null);
		assertDoesNotThrow(()-> customerOrderService.placeOrder(orderDTO1));

	}
	@Test
	public void placeOrderInValidTest4() throws EShopException {

		OrderDTO orderDTO1 = new OrderDTO();
		orderDTO1.setOrderId(111);
		orderDTO1.setCustomerEmailId("name@gmail.com");
		orderDTO1.setPaymentThrough("DEBIT_CARD");

		List<OrderedProductDTO> productList = new ArrayList<>();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductId(1);
		productDTO.setDiscount(5.00d);
		productDTO.setAvailableQuantity(1);
		productDTO.setQuantity(1);
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setPrice(1000.00d);
		productDTO.setBrand("Brand");
		orderDTO1.setOrderedProducts(productList);

		OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
		orderedProductDTO.setOrderedProductId(1);
		orderedProductDTO.setQuantity(13);
		orderedProductDTO.setProduct(productDTO);
		productList.add(orderedProductDTO);

		Mockito.when(customerService.getCustomerByEmailId(Mockito.anyString())).thenReturn(customerDTO);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(null);
		assertThrows(EShopException.class,()-> customerOrderService.placeOrder(orderDTO1));

	}
	@Test
	void testPlaceOrder_Success() throws EShopException {
		Mockito.when(customerService.getCustomerByEmailId("test@example.com")).thenReturn(customerDTO);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0);
			order.setOrderId(1);
			return order;
		});

		Integer orderId = customerOrderService.placeOrder(orderDTO);

		assertNotNull(orderId);
		assertEquals(1, orderId);
	}



	@Test
	public void getOrderDetailValidTest() throws EShopException {

		LocalDate date = LocalDate.of(2022, 02, 12);
		LocalTime time = LocalTime.of(18, 22);
		Order order = new Order();
		order.setOrderId(1234);
		order.setCustomerEmailId("customer@gmail.com");
		order.setDateOfOrder(LocalDateTime.now());
		order.setDiscount(10.00d);
		order.setTotalPrice(2300.00d);
		order.setOrderId(1234);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		order.setPaymentThrough(PaymentThrough.DEBIT_CARD);
		order.setDateOfDelivery(LocalDateTime.of(date, time));
		order.setDeliveryAddress("4th Line,Rome");

		List<OrderedProduct> orderedProductsList = new ArrayList<>();
		OrderedProduct orderedProduct = new OrderedProduct();
		orderedProduct.setOrderedProductId(5678);
		orderedProduct.setProductId(7890);
		orderedProduct.setQuantity(3);
		orderedProductsList.add(orderedProduct);
		order.setOrderedProducts(orderedProductsList);

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
		assertEquals(order.getOrderId(),
				customerOrderService.getOrderDetails(order.getOrderId()).getOrderId());

	}

	@Test
	public void getOrderInValidTest() {
		Integer orderId = 1234;

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.getOrderDetails(orderId));
		assertEquals("OrderService.ORDER_NOT_FOUND", excep.getMessage());
	}

	@Test
	public void getOrderInValidTest1() throws EShopException {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerEmailId("name@gmail.com");
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setAddress(" ");

		Mockito.when(customerService.getCustomerByEmailId(Mockito.anyString())).thenReturn(customerDTO);
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.placeOrder(orderDTO));
		assertEquals("OrderService.ADDRESS_NOT_AVAILABLE", excep.getMessage());

	}
	
	@Test
	public void updatePaymentThroughValidTest() {
		Integer OrderId = 1234;
		PaymentThrough paymentThrough = PaymentThrough.DEBIT_CARD;
		Order order = new Order();
		order.setOrderStatus(OrderStatus.PLACED);

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
		Assertions.assertDoesNotThrow(() -> customerOrderService.updatePaymentThrough(OrderId, paymentThrough));
	}

	@Test
	public void updatePaymentThroughInValidTest1() {
		Integer OrderId = 1234;
		PaymentThrough paymentThrough = PaymentThrough.DEBIT_CARD;

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.updatePaymentThrough(OrderId, paymentThrough));
		assertEquals("OrderService.ORDER_NOT_FOUND", excep.getMessage());
	}

	@Test
	public void updatePaymentThroughInValidTest2() {
		Integer OrderId = 1234;
		PaymentThrough paymentThrough = PaymentThrough.DEBIT_CARD;
		Order order = new Order();
		order.setOrderStatus(OrderStatus.CONFIRMED);

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.updatePaymentThrough(OrderId, paymentThrough));
		assertEquals("OrderService.TRANSACTION_ALREADY_DONE", excep.getMessage());
	}

	@Test
	public void updateOrderStatusValidTest() {
		Integer OrderId = 1234;
		OrderStatus orderStatus = OrderStatus.CANCELLED;
		Order order = new Order();
		order.setOrderStatus(OrderStatus.PLACED);

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
		Assertions.assertDoesNotThrow(() -> customerOrderService.updateOrderStatus(OrderId, orderStatus));

	}
	
	@Test
	public void updateOrderStatusInValidTest() {
		Integer OrderId = 1234;
		OrderStatus orderStatus = OrderStatus.CANCELLED;

		Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> customerOrderService.updateOrderStatus(OrderId, orderStatus));
		assertEquals("OrderService.ORDER_NOT_FOUND", excep.getMessage());

	}
	
}