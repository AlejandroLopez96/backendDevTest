package com.alex.zara.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8664374726369567006L;

	public NotFoundException(String message) {
		super(message);
	}
	
}
