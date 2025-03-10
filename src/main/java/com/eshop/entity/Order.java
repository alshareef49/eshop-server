package com.eshop.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.eshop.dto.OrderStatus;
import com.eshop.dto.PaymentThrough;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ORDER")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	private String customerEmailId;

	private LocalDateTime dateOfOrder;

	private Double discount;

	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	private PaymentThrough paymentThrough;

	private LocalDateTime dateOfDelivery;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId")
	private List<OrderedProduct> orderedProducts;

	private String deliveryAddress;

}