package com.rohan.lms.exceptionhandling.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

	public EntityAlreadyExistsException(String msg) {
		super(msg);
	}
}
