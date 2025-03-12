package com.eshop.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.eshop.dto.ProductDTO;
import com.eshop.entity.Product;
import com.eshop.exception.EShopException;

import com.eshop.repository.ProductRepository;
import com.eshop.service.CustomerProductService;
import com.eshop.service.CustomerProductServiceImpl;

@SpringBootTest
class CustomerProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CustomerProductService productService = new CustomerProductServiceImpl();

	@Test
	void getAllProductsValid() throws EShopException {

		// write your logic here

	}

	@Test
	public void getProductByValidTest() throws EShopException {

		Product product = new Product();
		product.setProductId(1005);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
		ProductDTO productDTO = productService.getProductById(product.getProductId());
		Assertions.assertEquals(product.getProductId(), productDTO.getProductId());

	}

	@Test
	public void getProductByInValidTest() throws EShopException {
		Product product = new Product();
		product.setProductId(105);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> productService.getProductById(product.getProductId()));
		Assertions.assertEquals("ProductService.PRODUCT_NOT_AVAILABLE", excep.getMessage());

	}

	@Test
	public void reduceAvailableQuantityvalidTest() throws EShopException {
		Product product = new Product();
		product.setProductId(1005);
		product.setAvailableQuantity(3);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
		Assertions.assertDoesNotThrow(
				() -> productService.reduceAvailableQuantity(product.getProductId(), product.getAvailableQuantity()));

	}

	@Test
	public void reduceAvailableQuantityInvalidTest() throws EShopException {
		Product product = new Product();
		product.setProductId(1005);
		product.setAvailableQuantity(3);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EShopException excep = Assertions.assertThrows(EShopException.class,
				() -> productService.reduceAvailableQuantity(product.getProductId(), product.getAvailableQuantity()));
		Assertions.assertEquals("ProductService.PRODUCT_NOT_AVAILABLE", excep.getMessage());

	}

}