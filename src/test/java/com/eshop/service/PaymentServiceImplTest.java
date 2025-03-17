package com.eshop.service;

import com.eshop.dto.OrderDTO;
import com.eshop.dto.TransactionDTO;
import com.eshop.dto.TransactionStatus;
import com.eshop.exception.EShopException;
import com.eshop.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void addTransactionTestFailed() throws EShopException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);
        transactionDTO.setOrder(null);
        transactionDTO.setTransactionDate(null);
        transactionDTO.setTotalPrice(0.0);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(1);

        assertThrows(EShopException.class, () -> paymentService.addTransaction(transactionDTO));
    }
    @Test
    public void addTransactionTestSuccess() throws EShopException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(111);
        transactionDTO.setOrder(orderDTO);
        transactionDTO.setTransactionDate(null);
        transactionDTO.setTotalPrice(0.0);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);

        assertDoesNotThrow(() -> paymentService.addTransaction(transactionDTO));
    }

}