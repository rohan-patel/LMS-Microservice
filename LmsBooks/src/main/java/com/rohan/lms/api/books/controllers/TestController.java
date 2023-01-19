package com.rohan.lms.api.books.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.books.model.Book;
import com.rohan.lms.api.books.model.Error;
import com.rohan.lms.api.books.repository.BookRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/books")
public class TestController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private BookRepository bookrepo;

	@GetMapping("/test")
	public ResponseEntity<Book> test(@RequestParam String isbn) {

//		if (true) {
//			throw new EntityNotFoundException("Hi there");
//		}
//		HttpHeaders httpHeaders = new HttpHeaders();
//		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
//		
//		try {
//			return restTemplate.exchange("http://localhost:8082/exception-ws/entity-not-found?message=Hi there", HttpMethod.GET, httpEntity, ResponseEntity.class).getBody();
//		} catch (HttpStatusCodeException e) {
//			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAs(Error.class));
//		}

		Book book = bookrepo.findById(isbn).get();

		List<String> author = bookrepo.findAuthorByIsbn(isbn);

		List<String> genres = bookrepo.findGenreByIsbn(isbn);

		Book book2 = new Book(isbn, book.getBookName(), genres, author, book.getYearOfPublication(), book.isBookAvailability(),
				book.getAddedAt(), book.getUpdatedAt());

		return new ResponseEntity<Book>(book2, HttpStatus.OK);

	}

}
