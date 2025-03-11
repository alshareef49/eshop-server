package com.eshop.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eshop.dto.ProductDTO;
import com.eshop.exception.EShopException;

import com.eshop.service.CustomerProductService;


@RestController
@RequestMapping(value = "/product-api")
public class ProductAPI {
	@Autowired
	private CustomerProductService customerProductService;
	@Autowired
	private Environment environment;

	Log logger = LogFactory.getLog(ProductAPI.class);

	
	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws EShopException {

		logger.info("Received a request to get all products");
		List<ProductDTO> products = customerProductService.getAllProducts();
		return new ResponseEntity<>(products, HttpStatus.OK);

	}

	@GetMapping(value = "/product/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) throws EShopException {

		logger.info("Received a request to get product details for product with productId as " + productId);
		ProductDTO product = customerProductService.getProductById(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PutMapping(value = "/update/{productId}")
	public ResponseEntity<String> reduceAvailableQuantity(@PathVariable Integer productId, @RequestBody String quantity)
			throws EShopException {

		logger.info("Received a request to update the available quantity  for product with productId as " + productId);
		customerProductService.reduceAvailableQuantity(productId, Integer.parseInt(quantity));
		return new ResponseEntity<>(environment.getProperty("ProductAPI.REDUCE_QUANTITY_SUCCESSFULL"), HttpStatus.OK);

	}
}