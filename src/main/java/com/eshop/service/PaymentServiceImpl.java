package com.eshop.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eshop.dto.CardDTO;
import com.eshop.dto.TransactionDTO;
import com.eshop.dto.TransactionStatus;
import com.eshop.models.Card;
import com.eshop.models.Transaction;
import com.eshop.exception.EShopException;
import com.eshop.repository.CardRepository;
import com.eshop.repository.TransactionRepository;
import com.eshop.utility.HashingUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Integer addCustomerCard(String customerEmailId, CardDTO cardDTO)
            throws EShopException, NoSuchAlgorithmException {

        cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
        Card newCard = new Card();
        newCard.setCardID(cardDTO.getCardId());
        newCard.setNameOnCard(cardDTO.getNameOnCard());
        newCard.setCardNumber(cardDTO.getCardNumber());
        newCard.setCardType(cardDTO.getCardType());
        newCard.setExpiryDate(cardDTO.getExpiryDate());
        newCard.setCvv(cardDTO.getHashCvv());
        newCard.setCustomerEmailId(cardDTO.getCustomerEmailId());

        cardRepository.save(newCard);
        return newCard.getCardID();
    }

    @Override
    public void updateCustomerCard(CardDTO cardDTO) throws EShopException, NoSuchAlgorithmException {

        Optional<Card> optionalCard = cardRepository.findById(cardDTO.getCardId());
        Card card = optionalCard.orElseThrow(() -> new EShopException("PaymentService.CARD_NOT_FOUND"));
        cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
        card.setCardID(cardDTO.getCardId());
        card.setNameOnCard(cardDTO.getNameOnCard());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardType(cardDTO.getCardType());
        card.setCvv(cardDTO.getHashCvv());
        card.setExpiryDate(cardDTO.getExpiryDate());
        card.setCustomerEmailId(cardDTO.getCustomerEmailId());

    }

    @Override
    public void deleteCustomerCard(String customerEmailId, Integer cardId) throws EShopException {

        List<Card> listOfCustomerCards = cardRepository.findByCustomerEmailId(customerEmailId);
        if (listOfCustomerCards.isEmpty())
            throw new EShopException("PaymentService.CUSTOMER_NOT_FOUND");

        Optional<Card> optionalCards = cardRepository.findById(cardId);
        Card card = optionalCards.orElseThrow(() -> new EShopException("PaymentService.CARD_NOT_FOUND"));
        cardRepository.delete(card);

    }

    @Override
    public CardDTO getCard(Integer cardId) throws EShopException {

        Optional<Card> optionalCards = cardRepository.findById(cardId);
        Card card = optionalCards.orElseThrow(() -> new EShopException("PaymentService.CARD_NOT_FOUND"));
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardId(card.getCardID());
        cardDTO.setNameOnCard(card.getNameOnCard());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setCardType(card.getCardType());
        cardDTO.setHashCvv(card.getCvv());
        cardDTO.setExpiryDate(card.getExpiryDate());
        cardDTO.setCustomerEmailId(card.getCustomerEmailId());
        return cardDTO;
    }

    @Override
    public List<CardDTO> getCustomerCardOfCardType(String customerEmailId, String cardType) throws EShopException {

        List<Card> cards = cardRepository.findByCustomerEmailIdAndCardType(customerEmailId, cardType);

        if (cards.isEmpty()) {
            throw new EShopException("PaymentService.CARD_NOT_FOUND");
        }
        List<CardDTO> cardDTOs = new ArrayList<CardDTO>();
        for (Card card : cards) {
            CardDTO cardDTO = new CardDTO();
            cardDTO.setCardId(card.getCardID());
            cardDTO.setNameOnCard(card.getNameOnCard());
            cardDTO.setCardNumber(card.getCardNumber());
            cardDTO.setCardType(card.getCardType());
            cardDTO.setHashCvv("XXX");
            cardDTO.setExpiryDate(card.getExpiryDate());
            cardDTO.setCustomerEmailId(card.getCustomerEmailId());

            cardDTOs.add(cardDTO);
        }
        return cardDTOs;
    }


    @Override
    public List<CardDTO> getCardsOfCustomer(String customerEmailId) throws EShopException {
        List<Card> cards = cardRepository.findByCustomerEmailId(customerEmailId);
        if (cards.isEmpty()) {
            throw new EShopException("PaymentService.CARD_NOT_FOUND");
        }

        List<CardDTO> cardDTOs = new ArrayList<CardDTO>();
        for (Card card : cards) {
            CardDTO cardDTO = new CardDTO();
            cardDTO.setCardId(card.getCardID());
            cardDTO.setNameOnCard(card.getNameOnCard());
            cardDTO.setCardNumber(card.getCardNumber());
            cardDTO.setCardType(card.getCardType());
            cardDTO.setHashCvv("XXX");
            cardDTO.setExpiryDate(card.getExpiryDate());
            cardDTO.setCustomerEmailId(card.getCustomerEmailId());

            cardDTOs.add(cardDTO);
        }
        return cardDTOs;
    }

    @Override
    public Integer addTransaction(TransactionDTO transactionDTO) throws EShopException {
        if (transactionDTO.getTransactionStatus().equals(TransactionStatus.TRANSACTION_FAILED)) {
            throw new EShopException("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING");
        }
        Transaction transaction = new Transaction();
        transaction.setCardId(transactionDTO.getCard().getCardId());

        transaction.setOrderId(transactionDTO.getOrder().getOrderId());
        transaction.setTotalPrice(transactionDTO.getTotalPrice());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
        transactionRepository.save(transaction);

        return transaction.getTransactionId();
    }

    @Override
    public TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
            throws EShopException, NoSuchAlgorithmException {
        if (!transactionDTO.getOrder().getCustomerEmailId().equals(customerEmailId)) {
            throw new EShopException("PaymentService.ORDER_DOES_NOT_BELONGS");
        }

        if (!transactionDTO.getOrder().getOrderStatus().equals("PLACED")) {
            throw new EShopException("PaymentService.TRANSACTION_ALREADY_DONE");

        }
        CardDTO cardDTO = getCard(transactionDTO.getCard().getCardId());
        if (!cardDTO.getCustomerEmailId().matches(customerEmailId)) {

            throw new EShopException("PaymentService.CARD_DOES_NOT_BELONGS");
        }
        if (!cardDTO.getCardType().equals(transactionDTO.getOrder().getPaymentThrough())) {

            throw new EShopException("PaymentService.PAYMENT_OPTION_SELECTED_NOT_MATCHING_CARD_TYPE");
        }
        if (cardDTO.getHashCvv().equals(HashingUtility.getHashValue(transactionDTO.getCard().getCvv().toString()))) {

            transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        } else {

            transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);

        }

        return transactionDTO;
    }
}
