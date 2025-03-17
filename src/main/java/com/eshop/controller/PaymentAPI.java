package com.eshop.controller;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.eshop.config.AuthCodeConfig;
import com.eshop.dto.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eshop.exception.EShopException;
import com.eshop.service.PaymentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/payment-api")
public class PaymentAPI {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate template;
    @Autowired
    private AuthCodeConfig authCodeConfig;

    Log logger = LogFactory.getLog(PaymentAPI.class);


    @PostMapping(value = "/customer/{customerEmailId}/order/{orderId}")
    public ResponseEntity<String> payForOrder(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{orderId.absent") @PathVariable("orderId") Integer orderId) throws NoSuchAlgorithmException, EShopException {

        logger.info("Recieved request to pay for order : " + orderId + " of customer : " + customerEmailId);

        ResponseEntity<OrderDTO> orderResponse = template.exchange("http://localhost:3333/EShop/order-api/order/" + orderId,
                HttpMethod.GET,
                authCodeConfig.getHeaderEntity(),
                OrderDTO.class);
        OrderDTO orderDTO = orderResponse.getBody();

        if (orderDTO == null) {
            throw new EShopException("OrderService.ORDER_NOT_FOUND");
        }
        PaymentProductRequest paymentProductRequest = new PaymentProductRequest();
        String name = orderDTO.getOrderedProducts().stream()
                .map(orderedProduct -> orderedProduct.getProduct().getName())
                .collect(Collectors.joining(", "));

        long totalQuantity = orderDTO.getOrderedProducts().stream()
                .mapToInt(OrderedProductDTO::getQuantity)
                .sum();

        paymentProductRequest.setName(name);
        paymentProductRequest.setCurrency("INR");
        paymentProductRequest.setAmount(orderDTO.getTotalPrice().longValue()*100);
        paymentProductRequest.setQuantity(totalQuantity);

        return template.exchange("http://localhost:8080/product/v1/checkout?orderId=" + orderId,
                HttpMethod.POST,
                authCodeConfig.getHeaderEntityWithBody(paymentProductRequest),
                String.class);
    }

}