package com.eshop.service;

import com.eshop.dto.TransactionDTO;
import com.eshop.dto.TransactionStatus;
import com.eshop.models.Transaction;
import com.eshop.exception.EShopException;
import com.eshop.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public Integer addTransaction(TransactionDTO transactionDTO) throws EShopException {
        if (transactionDTO.getTransactionStatus().equals(TransactionStatus.TRANSACTION_FAILED)) {
            throw new EShopException("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING");
        }
        Transaction transaction = new Transaction();
        transaction.setCardId(null);

        transaction.setOrderId(transactionDTO.getOrder().getOrderId());
        transaction.setTotalPrice(transactionDTO.getTotalPrice());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
        transactionRepository.save(transaction);

        return transaction.getTransactionId();
    }
}
