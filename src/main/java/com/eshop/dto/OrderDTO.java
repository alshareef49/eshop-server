package com.eshop.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class OrderDTO {

	private Integer orderId;
	@NotNull(message = "{email.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}")
	private String customerEmailId;
	private LocalDateTime dateOfOrder;
	private Double totalPrice;
	private String orderStatus;
	private Double discount;
	@NotNull(message = "{order.paymentthrough.absent}")
	@Pattern(regexp = "(DEBIT_CARD|CREDIT_CARD)", message = "{order.paymentthrough.invalid}")
	private String paymentThrough;
	private LocalDateTime dateOfDelivery;
	private String deliveryAddress;

	private List<OrderedProductDTO> orderedProducts;

}