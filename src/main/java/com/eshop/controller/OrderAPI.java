package com.eshop.controller;

import java.util.ArrayList;
import java.util.List;

import com.eshop.config.AuthCodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.eshop.dto.CartProductDTO;
import com.eshop.dto.OrderDTO;
import com.eshop.dto.OrderStatus;
import com.eshop.dto.OrderedProductDTO;
import com.eshop.dto.PaymentThrough;
import com.eshop.dto.ProductDTO;
import com.eshop.exception.EShopException;
import com.eshop.service.CustomerOrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


@RestController
@RequestMapping(value = "/order-api")
public class OrderAPI {
    @Autowired
    private CustomerOrderService orderService;
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate template;
    @Autowired
    private AuthCodeConfig authCodeConfig;

    @PostMapping(value = "/place-order")
    public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderDTO order) throws EShopException {

        ResponseEntity<CartProductDTO[]> cartProductDTOsResponse = template.exchange(
                "http://localhost:3333/EShop/cart-api/customer/" + order.getCustomerEmailId() + "/products",
                HttpMethod.GET,
                authCodeConfig.getHeaderEntity(),
                CartProductDTO[].class);

        CartProductDTO[] cartProductDTOs = cartProductDTOsResponse.getBody();
        template.exchange("http://localhost:3333/EShop/cart-api/customer/" + order.getCustomerEmailId() + "/products",
                HttpMethod.DELETE,
                authCodeConfig.getHeaderEntity(),
                String.class);
        List<OrderedProductDTO> orderedProductDTOs = new ArrayList<>();

        assert cartProductDTOs != null;
        for (CartProductDTO cartProductDTO : cartProductDTOs) {
            OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
            orderedProductDTO.setProduct(cartProductDTO.getProduct());
            orderedProductDTO.setQuantity(cartProductDTO.getQuantity());
            orderedProductDTOs.add(orderedProductDTO);
        }
        order.setOrderedProducts(orderedProductDTOs);

        Integer orderId = orderService.placeOrder(order);
        String modificationSuccessMsg = environment.getProperty("OrderAPI.ORDERED_PLACE_SUCCESSFULLY");

        return new ResponseEntity<String>(modificationSuccessMsg + orderId, HttpStatus.OK);

    }

    @GetMapping(value = "order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(
            @NotNull(message = "{orderId.absent}") @PathVariable Integer orderId) throws EShopException {
        OrderDTO orderDTO = orderService.getOrderDetails(orderId);
        for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {
            ResponseEntity<ProductDTO> productResponse = template.exchange(
                    "http://localhost:3333/EShop/product-api/product/" + orderedProductDTO.getProduct().getProductId(),
                    HttpMethod.GET,
                    authCodeConfig.getHeaderEntity(),
                    ProductDTO.class);
            orderedProductDTO.setProduct(productResponse.getBody());

        }

        return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.OK);

    }

    @GetMapping(value = "customer/{customerEmailId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersOfCustomer(
            @NotNull(message = "{email.absent}") @PathVariable String customerEmailId) throws EShopException {
        List<OrderDTO> orderDTOs = orderService.findOrdersByCustomerEmailId(customerEmailId);
        for (OrderDTO orderDTO : orderDTOs) {
            for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

                ResponseEntity<ProductDTO> productResponse = template
                        .exchange("http://localhost:3333/EShop/product-api/product/"
                                + orderedProductDTO.getProduct().getProductId(),
                                HttpMethod.GET,
                                authCodeConfig.getHeaderEntity(),
                                ProductDTO.class);
                orderedProductDTO.setProduct(productResponse.getBody());

            }

        }
        return new ResponseEntity<List<OrderDTO>>(orderDTOs, HttpStatus.OK);

    }

    @PutMapping(value = "order/{orderId}/update/order-status")
    public void updateOrderAfterPayment(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
                                        @RequestBody String transactionStatus) throws EShopException {
        if (transactionStatus.equals("TRANSACTION_SUCCESS")) {
            orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
            OrderDTO orderDTO = orderService.getOrderDetails(orderId);
            for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

                template.exchange("http://localhost:3333/EShop/product-api/update/"
                        + orderedProductDTO.getProduct().getProductId(),
                        HttpMethod.PUT,
                        authCodeConfig.getHeaderEntityWithBody(orderedProductDTO.getQuantity()),
                        String.class
                );
            }

        } else {
            orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        }
    }

    @PutMapping(value = "order/{orderId}/update/payment-through")
    public void updatePaymentOption(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
                                    @RequestBody String paymentThrough) throws EShopException {
        if (paymentThrough.equals("DEBIT_CARD")) {
            orderService.updatePaymentThrough(orderId, PaymentThrough.DEBIT_CARD);
        } else {

            orderService.updatePaymentThrough(orderId, PaymentThrough.CREDIT_CARD);
        }
    }

}