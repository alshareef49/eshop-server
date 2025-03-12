package com.eshop.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CARD")
public class Card {

	@Id
	@Column(name = "CARD_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cardID;

	@Column(name = "CARD_TYPE")
	private String cardType;

	@Column(name = "CARD_NUMBER")
	private String cardNumber;

	@Column(name = "CVV")
	private String cvv;

	@Column(name = "EXPIRY_DATE")
	private LocalDate expiryDate;

	@Column(name = "NAME_ON_CARD")
	private String nameOnCard;

	@Column(name = "CUSTOMER_EMAIL_ID")
	private String customerEmailId;
}