package com.eshop.service;

import java.util.Set;

import com.eshop.dto.CartProductDTO;
import com.eshop.dto.CustomerCartDTO;
import com.eshop.exception.EShopException;

public interface CustomerCartService {

	Integer addProductToCart(CustomerCartDTO customerCart) throws EShopException;

	Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws EShopException;

	void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
			throws EShopException;

	void deleteProductFromCart(String customerEmailId, Integer productId) throws EShopException;

	void deleteAllProductsFromCart(String customerEmailId) throws EShopException;

}