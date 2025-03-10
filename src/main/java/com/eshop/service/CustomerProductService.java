package com.eshop.service;

import java.util.List;

import com.eshop.dto.ProductDTO;
import com.eshop.exception.EShopException;

public interface CustomerProductService {
	List<ProductDTO> getAllProducts() throws EShopException;

	ProductDTO getProductById(Integer productId) throws EShopException;

	void reduceAvailableQuantity(Integer productId, Integer quantity) throws EShopException;
}