package com.eshop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.dto.CardDTO;
import com.eshop.dto.OrderDTO;
import com.eshop.dto.TransactionDTO;
import com.eshop.dto.TransactionStatus;
import com.eshop.entity.Card;
import com.eshop.exception.EShopException;
import com.eshop.repository.CardRepository;
import com.eshop.repository.TransactionRepository;
import com.eshop.service.PaymentService;
import com.eshop.service.PaymentServiceImpl;

@SpringBootTest
class CustomerCardsServiceTest {

	@Mock
	private CardRepository cardRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	PaymentService paymentService = new PaymentServiceImpl();

	@Test
	void updateCustomerCardValidTest() throws EShopException {
		Card card = new Card();
		card.setCardID(122);
		card.setNameOnCard("AIM");
		card.setCvv("466");
		card.setCardType("6642150005012186");
		card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
		card.setExpiryDate(LocalDate.of(2028, 02, 24));
		card.setCustomerEmailId("tom@gmail.com");
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(100);
		cardDTO.setCvv(466);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		Assertions.assertDoesNotThrow(() -> paymentService.updateCustomerCard(cardDTO));
	}

	@Test
	void updateCustomerCardInValidTest() throws EShopException {
		CardDTO cardDTO = new CardDTO();
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> paymentService.updateCustomerCard(cardDTO));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteCustomerCardValidTest() throws EShopException {
		List<Card> cards = new ArrayList<>();
		Card card = new Card();
		card.setCustomerEmailId("tom@gmail.com");
		card.setCardID(122);
		cards.add(card);
		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(cards);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		Assertions.assertDoesNotThrow(() -> paymentService.deleteCustomerCard("tom@gmail.com", 122));
	}

	@Test
	void deleteCustomerCardInValidTest1() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		Integer cardId = 100;
		Card card1 = new Card();
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card1));
		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(new ArrayList<>());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> paymentService.deleteCustomerCard(customerEmailId, cardId));
		Assertions.assertEquals("PaymentService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteCustomerCardInValidTest2() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		Integer cardId = 100;
		List<Card> cards = new ArrayList<>();
		Card card = new Card();
		cards.add(card);

		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(cards);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> paymentService.deleteCustomerCard(customerEmailId, cardId));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}

	@Test
	void getCardValidTest() throws EShopException {
		Card card = new Card();
		card.setCardID(100);
		card.setCvv("466");
		card.setExpiryDate(LocalDate.of(2028, 02, 24));
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		CardDTO cardDTO = paymentService.getCard(card.getCardID());
		Assertions.assertEquals(card.getCardID(), cardDTO.getCardId());
	}

	@Test
	void getCardInValidTest() throws EShopException {
		Integer cardId = 100;
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EShopException.class, () -> paymentService.getCard(cardId));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}

	@Test
	void getCardsOfCustomerValidTest() throws EShopException {
		
		// write your logic here
		
	}

	@Test
	void getCardsOfCustomerInValidTest1() throws EShopException {
		
		// write your logic here
		
	}

	@Test
	void addTransactionInValidTest1() throws EShopException {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);
		Exception exp = Assertions.assertThrows(EShopException.class,
				() -> paymentService.addTransaction(transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING", exp.getMessage());
	}

	@Test
	void addTransactionInvalidTest2() throws EShopException {
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Assertions.assertDoesNotThrow(() -> paymentService.addTransaction(transactionDTO));
	}

	@Test
	void authenticatePaymentOnInValidTest1() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		order.setCustomerEmailId("tom@gmail.com");
		order.setOrderStatus("CANCELLED");
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Exception e = Assertions.assertThrows(EShopException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());
	}

	@Test
	void authenticatePaymentOnInValidTest2() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		order.setCustomerEmailId("tom@gmail.com");
		order.setOrderStatus("CANCELLED");
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Exception e = Assertions.assertThrows(EShopException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());

	}

	@Test
	void authenticatePaymentInValidTest3() throws EShopException {
		String customerEmailId = "tom@gmail.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerEmailId("tom@gmail.com");
		orderDTO.setOrderStatus("PLACED");
		transactionDTO.setOrder(orderDTO);
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(1234);
		cardDTO.setCustomerEmailId("Customer@infy.com");
		transactionDTO.setCard(cardDTO);
		cardDTO.setCustomerEmailId("Customer@infy.com");
		transactionDTO.setCard(cardDTO);

		Card card = new Card();
		card.setCardType("CREDIT_CARD");
		card.setExpiryDate(LocalDate.of(2028, 02, 24));
		card.setNameOnCard("AIM");
		card.setCvv("466");
		card.setCardType("6642150005012186");
		card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
		card.setExpiryDate(LocalDate.of(2028, 02, 24));
		card.setCustomerEmailId("Customer@infy.com");

		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));

		Exception e = Assertions.assertThrows(EShopException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.CARD_DOES_NOT_BELONGS", e.getMessage());

	}
}
