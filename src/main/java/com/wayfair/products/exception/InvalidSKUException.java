package com.wayfair.products.exception;

import org.springframework.core.NestedRuntimeException;

public class InvalidSKUException extends NestedRuntimeException {

	private static final long serialVersionUID = 2713753075604983569L;

	public InvalidSKUException(String sku) {
		super("Invalid SKU:" + sku);
	}

}
