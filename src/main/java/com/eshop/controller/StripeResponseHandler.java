package com.eshop.controller;

import com.eshop.config.AuthCodeConfig;
import com.eshop.dto.OrderDTO;
import com.eshop.dto.TransactionDTO;
import com.eshop.dto.TransactionStatus;
import com.eshop.exception.EShopException;
import com.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
public class StripeResponseHandler {

    @Autowired
    private Environment environment;

    @Autowired
    AuthCodeConfig authCodeConfig;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate template;

    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam("orderId") String orderId, @RequestParam("token") String token) throws EShopException {
        AuthCodeConfig.TOKEN = token;
        template.exchange("http://localhost:3333/EShop/order-api/order/" + orderId + "/update/order-status",
                HttpMethod.PUT,
                authCodeConfig.getHeaderEntityWithBody("TRANSACTION_SUCCESS"),
                String.class);
        return setTransactionId(orderId,"TRANSACTION_SUCCESS");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> failure(@RequestParam("orderId") String orderId,@RequestParam("token") String token) throws EShopException {
        AuthCodeConfig.TOKEN = token;
        template.exchange("http://localhost:3333/EShop/order-api/order/" + orderId  + "/update/order-status",
                HttpMethod.PUT,
                authCodeConfig.getHeaderEntityWithBody("CANCELLED"),
                String.class);
        String failureMessage = "Payment failed! Please try again.";
        return new ResponseEntity<>(failureMessage, HttpStatus.BAD_REQUEST);
    }

    public  ResponseEntity<String> setTransactionId(String orderId,String transactionStatus) throws EShopException {
        ResponseEntity<OrderDTO> orderResponse = template.exchange("http://localhost:3333/EShop/order-api/order/" + orderId,
                HttpMethod.GET,
                authCodeConfig.getHeaderEntity(),
                OrderDTO.class);
        OrderDTO orderDTO = orderResponse.getBody();
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setOrder(orderDTO);
        assert orderDTO != null;
        transactionDTO.setTotalPrice(orderDTO.getTotalPrice());
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setTransactionStatus(TransactionStatus.valueOf(transactionStatus));
        int txnId = paymentService.addTransaction(transactionDTO);

        String message = environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_ONE") + orderDTO.getTotalPrice() + " "
                + environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_TWO") + " " + orderId + environment.getProperty("PaymentAPI.TRANSACTION_SUCCESSFULL_THREE") + txnId;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
