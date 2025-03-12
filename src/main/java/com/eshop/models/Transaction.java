package com.eshop.models;

import java.time.LocalDateTime;

import com.eshop.dto.TransactionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transactionId;
	private Integer orderId;
	private Integer cardId;
	private Double totalPrice;
	private LocalDateTime transactionDate;
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;

}