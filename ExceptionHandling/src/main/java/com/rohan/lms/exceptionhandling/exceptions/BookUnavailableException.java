package com.rohan.lms.exceptionhandling.exceptions;

public class BookUnavailableException extends RuntimeException {

	public BookUnavailableException(String msg) {
		super(msg);
	}
}
