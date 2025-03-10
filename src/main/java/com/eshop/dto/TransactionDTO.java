package com.eshop.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class TransactionDTO {

	private Integer transactionId;
	@Valid
	private OrderDTO order;
	@Valid
	private CardDTO card;
	private Double totalPrice;
	private LocalDateTime transactionDate;
	private TransactionStatus transactionStatus;

	@Override
	public String toString() {
		return "TransactionDTO [transactionId=" + transactionId + ", order=" + order + ", card=" + card
				+ ", totalPrice=" + totalPrice + ", transactionDate=" + transactionDate + ", transactionStatus="
				+ transactionStatus + "]";
	}
}
