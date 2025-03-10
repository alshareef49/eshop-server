package com.eshop.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.entity.CartProduct;
import com.eshop.entity.CustomerCart;
import com.eshop.exception.EShopException;
import com.eshop.repository.CartProductRepository;
import com.eshop.repository.CustomerCartRepository;
import com.eshop.service.CustomerCartService;
import com.eshop.service.CustomerCartServiceImpl;

@SpringBootTest
class CustomerCartServiceTest {
	
	// Write testcases here
	
	@Mock
	private CustomerCartRepository customerCartRepository;

	@Mock
	private CartProductRepository  cartProductRepository;

	@InjectMocks
	CustomerCartService customerCartService = new CustomerCartServiceImpl();

	@Test
	public void addProductToCartValidTest() throws EShopException {
		
		// write your logic here
		
	}

	@Test
	public void modifyQuantityOfProductInCartInValidTest() {
		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 1;
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());

	}
	
	@Test
	public void modifyQuantityOfProductInCartInvalidTest1() {

		String email = "name@gmail.com";
		Integer productId = 135;
		Integer quantity = 4;
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
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
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());
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
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE", exp.getMessage());

	}
	
	@Test
	void deleteProductFromCartValid() throws EShopException {

		// write your logic here
		
	}
	
	@Test
	void deleteProductFromCartValid1() throws EShopException {

		// write your logic here
		
	}
	
	@Test
	public void deleteProductFromCartInValidTest() {
		
		// write your logic here
		
	}

	@Test
	public void deleteProductFromCartInValidTest2() {
		
		// write your logic here
		
	}	

	@Test
	public void deleteProductFromCartInValidTest3() {
		String email = "name@gmail.com";
		Integer productId = 2345;
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCartId(234);
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		CartProduct cartProduct = new CartProduct();
		cartProduct.setCartProductId(6758);
		cartProduct.setProductId(2356);
		cartProduct.setQuantity(2300);
		cartProductSet.add(cartProduct);
		customerCart.setCartProducts(cartProductSet);

		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.deleteProductFromCart(email, productId));
		Assertions.assertEquals("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE", exp.getMessage());
	}

	@Test
	public void deleteAllProductsFromCartValidTest() {
		String email = "name@gmail.com";
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

	@Test
	public void deleteAllProductsFromCartIValidTest1() {
		String email = "name@gmail.com";
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}
	
	@Test
	public void deleteAllProductFromCartInValidTest2() {
		String email = "name@gmail.com";
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		customerCart.setCartProducts(cartProductSet);
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EShopException exp = Assertions.assertThrows(EShopException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());

	}	

}
