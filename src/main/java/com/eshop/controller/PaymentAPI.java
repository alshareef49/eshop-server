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

    @PostMapping(value = "/customer/{customerEmailId:.+}/cards")
    public ResponseEntity<String> addNewCard(@RequestBody CardDTO cardDTO,
                                             @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EShopException, NoSuchAlgorithmException {
        logger.info("Recieved request to add new  card for customer : " + customerEmailId);
        cardDTO.setCustomerEmailId(customerEmailId);

        int cardId;
        cardId = paymentService.addCustomerCard(customerEmailId, cardDTO);
        String message = environment.getProperty("PaymentAPI.NEW_CARD_ADDED_SUCCESS");
        String toReturn = message + cardId;
        toReturn = toReturn.trim();
        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }

    @PutMapping(value = "/update/card")
    public ResponseEntity<String> updateCustomerCard(@Valid @RequestBody CardDTO cardDTO)
            throws EShopException, NoSuchAlgorithmException {
        logger.info("Recieved request to update  card :" + cardDTO.getCardId() + " of customer : "
                + cardDTO.getCustomerEmailId());

        paymentService.updateCustomerCard(cardDTO);
        String modificationSuccessMsg = environment.getProperty("PaymentAPI.UPDATE_CARD_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }

    @DeleteMapping(value = "/customer/{customerEmailId:.+}/card/{cardID}/delete")
    public ResponseEntity<String> deleteCustomerCard(@PathVariable("cardID") Integer cardID,
                                                     @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EShopException {
        logger.info("Recieved request to delete  card :" + cardID + " of customer : " + customerEmailId);

        paymentService.deleteCustomerCard(customerEmailId, cardID);
        String modificationSuccessMsg = environment.getProperty("PaymentAPI.CUSTOMER_CARD_DELETED_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

    }


    @GetMapping(value = "/customer/{customerEmailId}/card-type/{cardType}")
    public ResponseEntity<List<CardDTO>> getCardsOfCustomer(@PathVariable String customerEmailId,
                                                            @PathVariable String cardType) throws EShopException {
        logger.info("Recieved request to fetch  cards of customer : " + customerEmailId + " having card type as: "
                + cardType);

        List<CardDTO> cardDTOs = paymentService.getCustomerCardOfCardType(customerEmailId, cardType);

        return new ResponseEntity<List<CardDTO>>(cardDTOs, HttpStatus.OK);

    }


    @PostMapping(value = "/customer/{customerEmailId}/order/{orderId}")
    public ResponseEntity<String> payForOrder(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{orderId.absent") @PathVariable("orderId") Integer orderId,
            @Valid @RequestBody CardDTO cardDTO) throws NoSuchAlgorithmException, EShopException {

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