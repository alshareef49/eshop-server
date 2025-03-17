package com.eshop.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.eshop.dto.CartProductDTO;
import com.eshop.dto.CustomerCartDTO;
import com.eshop.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.models.CartProduct;
import com.eshop.models.CustomerCart;
import com.eshop.exception.EShopException;
import com.eshop.repository.CartProductRepository;
import com.eshop.repository.CustomerCartRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerCartServiceTest {

	
	@Mock
	private CustomerCartRepository customerCartRepository;

	@Mock
	private CartProductRepository  cartProductRepository;

	@InjectMocks
	CustomerCartService customerCartService = new CustomerCartServiceImpl();

	@Test
	public void addProductToCartValidTest() throws EShopException {
		
		CustomerCartDTO customerCartDTO = new CustomerCartDTO();
		customerCartDTO.setCustomerEmailId("name@gmail.com");
		Set<CartProductDTO> cartProducts = new HashSet<>();
		CartProductDTO  cartProduct = new CartProductDTO();
		cartProduct.setQuantity(10);
		cartProduct.setCartProductId(1);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand("apple");
		productDTO.setProductId(1000);
		productDTO.setName("iphone");
		productDTO.setDescription("iphonex");
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setDiscount(10.00d);
		productDTO.setPrice(100.00d);
		productDTO.setAvailableQuantity(19);
		productDTO.setQuantity(10);
		cartProduct.setProduct(productDTO);
		cartProducts.add(cartProduct);
		customerCartDTO.setCartProducts(cartProducts);


		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		when(customerCartRepository.save(Mockito.any())).thenReturn(null);
		Assertions.assertDoesNotThrow(()->customerCartService.addProductToCart(customerCartDTO));
		
	}

	@Test
	public void addProductToCartValidTest2() throws EShopException {

		CustomerCartDTO customerCartDTO = new CustomerCartDTO();
		customerCartDTO.setCustomerEmailId("name@gmail.com");
		Set<CartProductDTO> cartProducts = new HashSet<>();
		CartProductDTO  cartProduct = new CartProductDTO();
		cartProduct.setQuantity(10);
		cartProduct.setCartProductId(1);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand("apple");
		productDTO.setProductId(1000);
		productDTO.setName("iphone");
		productDTO.setDescription("iphonex");
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setSellerEmailId("name@gmail.com");
		productDTO.setDiscount(10.00d);
		productDTO.setPrice(100.00d);
		productDTO.setAvailableQuantity(19);
		productDTO.setQuantity(10);
		cartProduct.setProduct(productDTO);
		cartProducts.add(cartProduct);
		customerCartDTO.setCartProducts(cartProducts);

		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId("name@gmail.com");
		Set<CartProduct> cartProductSet = new HashSet<>();
		CartProduct cartProduct1 = new CartProduct();
		cartProduct1.setCartProductId(1);
		cartProduct1.setProductId(1000);
		cartProduct1.setQuantity(10);
		cartProductSet.add(cartProduct1);
		customerCart.setCartProducts(cartProductSet);

		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.of(customerCart));
		when(customerCartRepository.save(Mockito.any())).thenReturn(null);
		Assertions.assertDoesNotThrow(()->customerCartService.addProductToCart(customerCartDTO));

	}

	@Test
	public void modifyQuantityOfProductInCartInValidTest() {
		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 1;
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());

	}
	
	@Test
	public void modifyQuantityOfProductInCartInvalidTest1() {

		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 4;
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}
	
	@Test
	public void modifyQuantityOfProductInCartInValidTest2() {
		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 4;
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		customerCart.setCartProducts(cartProductSet);
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());
	}

	@Test
	public void modifyQuantityOfProductInCartInvalidTest3() {
		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 4;
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		CartProduct cartProduct = new CartProduct();
		cartProduct.setProductId(132);
		cartProduct.setQuantity(87);
		cartProductSet.add(cartProduct);
		customerCart.setCartProducts(cartProductSet);
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		assertEquals("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE", exp.getMessage());

	}

	@Test
	void getProductsFromCart_ShouldReturnCartProducts_WhenCartExistsAndHasProducts() throws EShopException {
		String email = "test@example.com";
		CustomerCart mockCart = new CustomerCart();
		CartProduct cartProduct = new CartProduct();
		cartProduct.setCartProductId(1);
		cartProduct.setQuantity(2);
		cartProduct.setProductId(101);

		mockCart.setCartProducts(Set.of(cartProduct));

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.of(mockCart));

		Set<CartProductDTO> cartProducts = customerCartService.getProductsFromCart(email);

		assertNotNull(cartProducts);
		assertEquals(1, cartProducts.size());
		CartProductDTO cartProductDTO = cartProducts.iterator().next();
		assertEquals(1, cartProductDTO.getCartProductId());
		assertEquals(2, cartProductDTO.getQuantity());
		assertEquals(101, cartProductDTO.getProduct().getProductId());
	}

	@Test
	void getProductsFromCart_ShouldThrowException_WhenCartNotFound() {

		String email = "test@example.com";

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.empty());

		EShopException exception = assertThrows(EShopException.class,
				() -> customerCartService.getProductsFromCart(email));

		assertEquals("CustomerCartService.NO_CART_FOUND", exception.getMessage());
	}

	@Test
	void getProductsFromCart_ShouldThrowException_WhenCartHasNoProducts() {
		String email = "test@example.com";
		CustomerCart emptyCart = new CustomerCart();
		emptyCart.setCartProducts(Collections.emptySet());

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.of(emptyCart));

		EShopException exception = assertThrows(EShopException.class,
				() -> customerCartService.getProductsFromCart(email));

		assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exception.getMessage());
	}

	@Test
	void deleteProductFromCart_ShouldDeleteProduct_WhenProductExistsInCart() throws EShopException {
		String email = "test@example.com";
		Integer productId = 101;
		CartProduct cartProduct = new CartProduct();
		cartProduct.setCartProductId(productId);

		CustomerCart cart = new CustomerCart();
		cart.setCartProducts(new HashSet<>(Set.of(cartProduct)));

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.of(cart));

		assertDoesNotThrow(() -> customerCartService.deleteProductFromCart(email, productId));

		assertFalse(cart.getCartProducts().contains(cartProduct));
		verify(cartProductRepository, times(1)).deleteById(productId);
	}

	@Test
	void deleteProductFromCart_ShouldThrowException_WhenCartNotFound() {
		// Arrange
		String email = "test@example.com";
		Integer productId = 101;

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.empty());

		// Act & Assert
		EShopException exception = assertThrows(EShopException.class,
				() -> customerCartService.deleteProductFromCart(email, productId));

		assertEquals("CustomerCartService.NO_CART_FOUND", exception.getMessage());
	}

	@Test
	void deleteProductFromCart_ShouldThrowException_WhenCartHasNoProducts() {

		String email = "test@example.com";
		Integer productId = 101;
		CustomerCart cart = new CustomerCart();
		cart.setCartProducts(Collections.emptySet());

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.of(cart));

		EShopException exception = assertThrows(EShopException.class,
				() -> customerCartService.deleteProductFromCart(email, productId));

		assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exception.getMessage());
	}


	@Test
	void deleteProductFromCart_ShouldNotDeleteProduct_WhenProductNotInCart() throws EShopException {

		String email = "test@example.com";
		Integer existingProductId = 101;
		Integer nonExistingProductId = 202;

		CartProduct cartProduct = new CartProduct();
		cartProduct.setCartProductId(existingProductId);

		CustomerCart cart = new CustomerCart();
		cart.setCartProducts(new HashSet<>(Set.of(cartProduct)));

		when(customerCartRepository.findByCustomerEmailId(email)).thenReturn(Optional.of(cart));

		assertDoesNotThrow(() -> customerCartService.deleteProductFromCart(email, nonExistingProductId));

		assertEquals(1, cart.getCartProducts().size()); // Product count should remain same
		verify(cartProductRepository, never()).deleteById(nonExistingProductId); // Ensure delete is not called
	}


	@Test
	public void deleteAllProductsFromCartValidTest() {
		String email = "name@gmail.com";
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

	@Test
	public void deleteAllProductsFromCartIValidTest1() {
		String email = "name@gmail.com";
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}
	
	@Test
	public void deleteAllProductFromCartInValidTest2() {
		String email = "name@gmail.com";
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		customerCart.setCartProducts(cartProductSet);
		when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());

	}	

}
