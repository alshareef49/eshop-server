package com.eshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.eshop.config.AuthCodeConfig;
import com.eshop.dto.*;
import com.eshop.exception.EShopException;
import com.eshop.service.CustomerOrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

class OrderAPITest {

    @InjectMocks
    private OrderAPI orderAPI;

    @Mock
    private CustomerOrderService orderService;

    @Mock
    private Environment environment;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthCodeConfig authCodeConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_Success() throws EShopException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerEmailId("test@example.com");
        CartProductDTO cartProduct = new CartProductDTO();
        cartProduct.setQuantity(2);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);
        cartProduct.setProduct(productDTO);

        ResponseEntity<CartProductDTO[]> cartResponse = new ResponseEntity<>(new CartProductDTO[]{cartProduct}, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CartProductDTO[].class)))
                .thenReturn(cartResponse);

        when(orderService.placeOrder(any(OrderDTO.class))).thenReturn(1001);
        when(environment.getProperty("OrderAPI.ORDERED_PLACE_SUCCESSFULLY")).thenReturn("Order placed successfully: ");

        ResponseEntity<String> response = orderAPI.placeOrder(orderDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed successfully: 1001", response.getBody());

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(String.class));
    }

    @Test
    void testGetOrderDetails_Success() throws EShopException {

        OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);
        orderedProductDTO.setProduct(productDTO);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1001);
        orderDTO.setOrderedProducts(List.of(orderedProductDTO));

        when(orderService.getOrderDetails(1001)).thenReturn(orderDTO);

        ResponseEntity<ProductDTO> productResponse = new ResponseEntity<>(productDTO, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductDTO.class)))
                .thenReturn(productResponse);

        ResponseEntity<OrderDTO> response = orderAPI.getOrderDetails(1001);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1001, response.getBody().getOrderId());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductDTO.class));
    }


    @Test
    void testGetOrdersOfCustomer_Success() throws EShopException {

        OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);
        orderedProductDTO.setProduct(productDTO);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1001);
        orderDTO.setOrderedProducts(List.of(orderedProductDTO));

        when(orderService.findOrdersByCustomerEmailId("test@example.com")).thenReturn(List.of(orderDTO));

        ResponseEntity<ProductDTO> productResponse = new ResponseEntity<>(productDTO, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductDTO.class)))
                .thenReturn(productResponse);

        ResponseEntity<List<OrderDTO>> response = orderAPI.getOrdersOfCustomer("test@example.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductDTO.class));
    }

    @Test
    void testUpdateOrderAfterPayment_Success() throws EShopException {
        // Mock order
        OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);
        orderedProductDTO.setProduct(productDTO);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1001);
        orderDTO.setOrderedProducts(List.of(orderedProductDTO));

        when(orderService.getOrderDetails(1001)).thenReturn(orderDTO);

        orderAPI.updateOrderAfterPayment(1001, "TRANSACTION_SUCCESS");

        verify(orderService, times(1)).updateOrderStatus(1001, OrderStatus.CONFIRMED);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(), eq(String.class));
    }


    @Test
    void testUpdatePaymentOption_Success() throws EShopException {
        orderAPI.updatePaymentOption(1001, "DEBIT_CARD");
        verify(orderService, times(1)).updatePaymentThrough(1001, PaymentThrough.DEBIT_CARD);

        orderAPI.updatePaymentOption(1001, "CREDIT_CARD");
        verify(orderService, times(1)).updatePaymentThrough(1001, PaymentThrough.CREDIT_CARD);
    }
}
