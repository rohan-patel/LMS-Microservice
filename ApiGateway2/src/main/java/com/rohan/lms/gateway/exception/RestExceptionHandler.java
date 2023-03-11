package com.rohan.lms.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rohan.lms.gateway.model.Error;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(JwtTokenIncorrectStructureException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	private Error handleJwtIncorrectStructureException(JwtTokenIncorrectStructureException ex) {
		return new Error(HttpStatus.UNAUTHORIZED, "Incorre JWT Structure", ex.getMessage());
	}

	@ExceptionHandler(JwtTokenMalformedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	private Error handleJwtMalformedException(JwtTokenMalformedException ex) {
		return new Error(HttpStatus.UNAUTHORIZED, "JWT token malformed", ex.getMessage());
	}

	@ExceptionHandler(JwtTokenMissingException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	private Error handleJwtMissingException(JwtTokenMissingException ex) {
		return new Error(HttpStatus.UNAUTHORIZED, "Missing JWT", ex.getMessage());
	}

}
