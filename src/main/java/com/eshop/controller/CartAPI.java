package com.eshop.controller;

import java.util.Set;

import com.eshop.config.AuthCodeConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import com.eshop.dto.CartProductDTO;
import com.eshop.dto.CustomerCartDTO;
import com.eshop.dto.ProductDTO;
import com.eshop.exception.EShopException;
import com.eshop.service.CustomerCartService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@RestController
@RequestMapping(value = "/cart-api")
public class CartAPI {

    @Autowired
    private CustomerCartService customerCartService;
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate template;

    @Autowired
    private AuthCodeConfig authCodeConfig;

    Log logger = LogFactory.getLog(CartAPI.class);


    @PostMapping(value = "/products")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCartDTO customerCartDTO)
            throws EShopException {
        int cartId = customerCartService.addProductToCart(customerCartDTO);
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_ADDED_TO_CART_SUCCESS") + cartId;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<Set<CartProductDTO>> getProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EShopException {
        logger.info("Received a request to get products details from " + customerEmailId + " cart");

        Set<CartProductDTO> cartProductDTOs = customerCartService.getProductsFromCart(customerEmailId);
        for (CartProductDTO cartProductDTO : cartProductDTOs) {
            logger.info("Product call");
            ProductDTO productDTO = template.exchange(
                    "http://localhost:3333/EShop/product-api/product/" + cartProductDTO.getProduct().getProductId(),
                    HttpMethod.GET,
                    authCodeConfig.getHeaderEntity(),
                    ProductDTO.class).getBody();
            cartProductDTO.setProduct(productDTO);
            logger.info("Product complete");

        }
        return new ResponseEntity<Set<CartProductDTO>>(cartProductDTOs, HttpStatus.OK);

    }


    @DeleteMapping(value = "/customer/{customerEmailId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{invalid.email.format}") @PathVariable("productId") Integer productId)
            throws EShopException {
        customerCartService.deleteProductFromCart(customerEmailId, productId);
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_DELETED_FROM_CART_SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PutMapping(value = "/customer/{customerEmailId}/product/{productId}")
    public ResponseEntity<String> modifyQuantityOfProductInCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{invalid.email.format}") @PathVariable("productId") Integer productId,
            @RequestBody String quantity) throws EShopException {
        logger.info("Received a request to modify the quantity of +" + productId + " prouct from  " + customerEmailId
                + " cart");

        customerCartService.modifyQuantityOfProductInCart(customerEmailId, productId, Integer.parseInt(quantity));
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_QUANTITY_UPDATE_FROM_CART_SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @DeleteMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<String> deleteAllProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EShopException {
        logger.info("Received a request to clear " + customerEmailId + " cart");

        customerCartService.deleteAllProductsFromCart(customerEmailId);
        String message = environment.getProperty("CustomerCartAPI.ALL_PRODUCTS_DELETED");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

}