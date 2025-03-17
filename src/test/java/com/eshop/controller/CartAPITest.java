package com.eshop.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eshop.config.AuthCodeConfig;
import com.eshop.dto.CartProductDTO;
import com.eshop.dto.CustomerCartDTO;
import com.eshop.dto.ProductDTO;
import com.eshop.service.CustomerCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

class CartAPITest {

    private MockMvc mockMvc;

    @Mock
    private CustomerCartService customerCartService;

    @Mock
    private Environment environment;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthCodeConfig authCodeConfig;

    @InjectMocks
    private CartAPI cartAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartAPI).build();
    }

    @Test
    void testAddProductToCart_Success() throws Exception {
        CustomerCartDTO customerCartDTO = new CustomerCartDTO();
        when(customerCartService.addProductToCart(any())).thenReturn(101);
        when(environment.getProperty("CustomerCartAPI.PRODUCT_ADDED_TO_CART_SUCCESS")).thenReturn("Product added successfully. Cart ID: ");

        mockMvc.perform(post("/cart-api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductsFromCart_Success() throws Exception {
        String customerEmail = "test@example.com";
        CartProductDTO cartProductDTO = new CartProductDTO();
        cartProductDTO.setProduct(new ProductDTO());
        Set<CartProductDTO> cartProducts = new HashSet<>();
        cartProducts.add(cartProductDTO);

        when(customerCartService.getProductsFromCart(customerEmail)).thenReturn(cartProducts);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductDTO.class)))
                .thenReturn(new ResponseEntity<>(new ProductDTO(), HttpStatus.OK));

        mockMvc.perform(get("/cart-api/customer/test@example.com/products"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProductFromCart_Success() throws Exception {
        String customerEmail = "test@example.com";
        Integer productId = 1;

        doNothing().when(customerCartService).deleteProductFromCart(customerEmail, productId);
        when(environment.getProperty("CustomerCartAPI.PRODUCT_DELETED_FROM_CART_SUCCESS")).thenReturn("Product deleted successfully.");

        mockMvc.perform(delete("/cart-api/customer/test@example.com/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully."));
    }

    @Test
    void testModifyQuantityOfProductInCart_Success() throws Exception {
        String customerEmail = "test@example.com";
        Integer productId = 1;
        String quantity = "5";

        doNothing().when(customerCartService).modifyQuantityOfProductInCart(customerEmail, productId, Integer.parseInt(quantity));
        when(environment.getProperty("CustomerCartAPI.PRODUCT_QUANTITY_UPDATE_FROM_CART_SUCCESS")).thenReturn("Product quantity updated.");

        mockMvc.perform(put("/cart-api/customer/test@example.com/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quantity))
                .andExpect(status().isOk())
                .andExpect(content().string("Product quantity updated."));
    }

    @Test
    void testDeleteAllProductsFromCart_Success() throws Exception {
        String customerEmail = "test@example.com";

        doNothing().when(customerCartService).deleteAllProductsFromCart(customerEmail);
        when(environment.getProperty("CustomerCartAPI.ALL_PRODUCTS_DELETED")).thenReturn("All products deleted from cart.");

        mockMvc.perform(delete("/cart-api/customer/test@example.com/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("All products deleted from cart."));
    }

}
