package com.eshop.service;

import java.util.List;

import com.eshop.dto.OrderDTO;
import com.eshop.dto.OrderStatus;
import com.eshop.dto.PaymentThrough;
import com.eshop.exception.EShopException;

public interface CustomerOrderService {
	
	Integer placeOrder(OrderDTO orderDTO) throws EShopException;

	OrderDTO getOrderDetails(Integer orderId) throws EShopException;

	List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws EShopException;

	void updateOrderStatus(Integer orderId, OrderStatus orderStatus) throws EShopException;

	void updatePaymentThrough(Integer orderId, PaymentThrough paymentThrough) throws EShopException;

}