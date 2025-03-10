package com.eshop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {

	private Integer productId;
	@Pattern(regexp = "([A-Za-z0-9-.])+(\\s[A-Za-z0-9-.]+)*", message = "{product.invalid.name}")
	private String name;
	@Size(min = 10, message = "{product.invalid.description}")
	private String description;
	private String category;
	@Size(min = 3, message = "{product.invalid.brand}")
	private String brand;
	@Min(value = 1, message = "{product.invalid.price}")
	private Double price;
	@Min(value = 0, message = "{product.invalid.discount}")
	@Max(value = 99, message = "{product.invalid.discount}")
	private Double discount;
	@Min(value = 1, message = "{product.invalid.quantity}")
	private Integer quantity;
	private String errorMessage;
	private String successMessage;
	private String sellerEmailId;
	private Integer availableQuantity;

}