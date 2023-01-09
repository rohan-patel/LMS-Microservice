package com.rohan.lms.exceptionhandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

import com.rohan.lms.exceptionhandling.model.Error;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	private ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException ex) {
		Error error = new Error(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityAlreadyExistsException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	private Error handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
		return new Error(HttpStatus.BAD_REQUEST, "Entity already exists", ex.getMessage());
	}

	@ExceptionHandler(InsufficientDataException.class)
	@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
	private Error handleInsufficientDataException(InsufficientDataException ex) {
		return new Error(HttpStatus.NOT_ACCEPTABLE, "Data provided in payload is not sufficient", ex.getMessage());
	}

	@ExceptionHandler(BookUnavailableException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	private Error handleBookUnavailableException(BookUnavailableException ex) {
		return new Error(HttpStatus.BAD_REQUEST, "The book is unavailable", ex.getMessage());
	}

}
