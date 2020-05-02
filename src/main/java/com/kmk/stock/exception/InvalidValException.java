package com.kmk.stock.exception;

public class InvalidValException extends RuntimeException{
	
	/**
	 * Custom Exception class for invalid Stock or Trade details 
	 */
	private static final long serialVersionUID = 1135688719642730441L;

	public InvalidValException(String message) {
		super(message);
	}
}
