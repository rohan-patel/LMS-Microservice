package com.rohan.lms.api.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.books.model.Error;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/books")
public class TestController {
	
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/test")
	public ResponseEntity test() {
		
//		if (true) {
//			throw new EntityNotFoundException("Hi there");
//		}
		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		
		try {
			return restTemplate.exchange("http://localhost:8082/exception-ws/entity-not-found?message=Hi there", HttpMethod.GET, httpEntity, ResponseEntity.class).getBody();
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAs(Error.class));
		}
	}
	
}
