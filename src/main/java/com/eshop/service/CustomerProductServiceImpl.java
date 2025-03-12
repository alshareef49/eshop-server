package com.eshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.eshop.dto.ProductDTO;
import com.eshop.entity.Product;
import com.eshop.exception.EShopException;

import com.eshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {

	@Autowired
	private ProductRepository productRepository;

	
	@Override
	public List<ProductDTO> getAllProducts() throws EShopException {
		List<Product> products = (List<Product>) productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(Product product : products) {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setAvailableQuantity(product.getAvailableQuantity());
            productDTOs.add(productDTO);
		}
		return productDTOs;
	}

	@Override
	public ProductDTO getProductById(Integer productId) throws EShopException {

		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new EShopException("ProductService.PRODUCT_NOT_AVAILABLE"));

		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand(product.getBrand());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductId(product.getProductId());
		productDTO.setAvailableQuantity(product.getAvailableQuantity());

		return productDTO;
	}

	@Override
	public void reduceAvailableQuantity(Integer productId, Integer quantity) throws EShopException {
		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new EShopException("ProductService.PRODUCT_NOT_AVAILABLE"));
		product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
	}
}