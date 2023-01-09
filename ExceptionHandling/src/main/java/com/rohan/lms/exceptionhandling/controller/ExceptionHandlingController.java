package com.rohan.lms.exceptionhandling.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.lms.exceptionhandling.exceptions.EntityAlreadyExistsException;
import com.rohan.lms.exceptionhandling.exceptions.InsufficientDataException;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class ExceptionHandlingController {

	@GetMapping("/entity-not-found")
	public void entityNotFound(@RequestParam String message) {
		throw new EntityAlreadyExistsException(message);
	}
	
	@GetMapping("/insufficient-data")
	public void insufficientData(@RequestParam String message) {
		throw new InsufficientDataException(message);
	}
	
	@GetMapping("/entity-already-exists")
	public void entityAlreadyExists(@RequestParam String message) {
		throw new EntityAlreadyExistsException(message);
	}
}
