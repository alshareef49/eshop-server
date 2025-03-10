package com.eshop.exception;

import java.io.Serial;

public class EShopException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	public EShopException(String message) {
		super(message);
	}

}