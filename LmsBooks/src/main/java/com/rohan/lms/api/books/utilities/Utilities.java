package com.rohan.lms.api.books.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.books.model.Error;

@Component
public class Utilities {
	
	@Autowired
	private static RestTemplate restTemplate;

	public static ResponseEntity buildRestTemplate(String message, String exception) {

		String url = "http://localhost:8082/exception-ws/" + exception + "?message=" + message;

		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

		try {
			return restTemplate.exchange(url, HttpMethod.GET, httpEntity, ResponseEntity.class).getBody();
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAs(Error.class));
		}

	}
}
