package com.rohan.lms.api.bookborrow.controllers;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.bookborrow.repository.BorrowBookRepository;
import com.rohan.lms.api.bookborrow.model.Book;
import com.rohan.lms.api.bookborrow.model.BorrowBook;
import com.rohan.lms.api.bookborrow.model.Error;

@RestController
@RequestMapping("/book")
public class BorrowBookController {

	@Autowired
	private BorrowBookRepository borrowBookRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${springjms.requestQueue}")
	private String requestQueue;
	@Value("${springjms.replyQueue}")
	private String replyQueue;

	private Book book = null;

	@PostMapping("/borrow")
	public ResponseEntity<BorrowBook> borrowBook(@RequestBody BorrowBook borrowBook) {

		jmsTemplate.convertAndSend(requestQueue, (String) borrowBook.getIsbn());

		while (book == null);

		Book book2 = modelMapper.map(book, Book.class);
		System.out.println(book2.getBookName());

		return null;
	}
	

	public ResponseEntity exceptionHanlding(String message, String exception) {

		String url = "http://localhost:8082/exception-ws/" + exception + "?message=" + message;

		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

		try {
			return restTemplate.exchange(url, HttpMethod.GET, httpEntity, ResponseEntity.class).getBody();
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAs(Error.class));
		}

	}


	@JmsListener(destination = "${springjms.replyQueue}")
	public void getBook(Book book) {
		this.book = book;
	}
}
