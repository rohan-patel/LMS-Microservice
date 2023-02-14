package com.rohan.lms.api.bookborrow.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.rohan.lms.api.bookborrow.utilities.Utilities;

import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;

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
	private Environment env;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${springjms.requestQueue}")
	private String requestQueue;
	@Value("${springjms.replyQueue}")
	private String replyQueue;
	@Value("${microservices.communication.book.checks}")
	private String checksId;
	@Value("${microservices.communication.book.updateAvailabilityToFalse}")
	private String updateToFalseId;
	@Value("${microservices.communication.book.updateAvailabilityToTrue}")
	private String updateToTrueId;

	private MapMessage checkResponses = null;

	@PostMapping("/borrow")
	public ResponseEntity<BorrowBook> borrowBook(@RequestBody BorrowBook borrowBook) throws JMSException {

		String isbn = borrowBook.getIsbn();
		jmsTemplate.convertAndSend(requestQueue, isbn);

		jmsTemplate.convertAndSend(requestQueue, checksId);

		while (checkResponses == null)
			;

		if (checkResponses.getInt("executedCheck") == 1) {
			System.out.println("entityNotFound: " + checkResponses.getBoolean("entityNotFound"));

			String message = "Book with given ISBN: " + isbn
					+ ", was not found in the database. Please check the entered ISBN";

			return exceptionHanlding(message, "entity-not-found");

		} else if (checkResponses.getInt("executedCheck") == 2) {
			System.out.println("bookUnavailable: " + checkResponses.getBoolean("bookUnavailable"));

			String message = "The requested book with ISBN: " + isbn
					+ " is not available right now. Most probably it would be available on "
					+ borrowBookRepo.findByIsbn(isbn).get().getToBeReturnedOn();

			return exceptionHanlding(message, "book-unavailable");
		}

		Timestamp borrowedOn = new Timestamp(new Date().getTime());
		BorrowBook borrowedBook = new BorrowBook(isbn, borrowBook.getMemberId(), borrowBook.getEmployeeId(), borrowedOn,
				Utilities.addDays(borrowedOn, 30));

		borrowBookRepo.save(borrowedBook);

		jmsTemplate.convertAndSend(requestQueue, updateToFalseId);

		System.out.println("Record found and is available");
		System.out.println("Checks Done");

		return new ResponseEntity<BorrowBook>(borrowedBook, HttpStatus.OK);

	}

	public ResponseEntity<String> returnBook(@RequestParam String isbn) throws JMSException {

		Optional<BorrowBook> borrowBook = borrowBookRepo.findByIsbn(isbn);

		if (borrowBook.isEmpty()) {
			String message = "The Book with ISBN: " + isbn
					+ "is either not borrowed by any member or is not present in the database. Please check the entered ISBN.";
			return exceptionHanlding(message, "entity-not-found");
		}

		BorrowBook borrowedBook = borrowBook.get();

		borrowedBook.setBookReturned(true);
		borrowedBook.setReturnedOn(new Timestamp(new Date().getTime()));
		borrowedBook.setToBeReturnedOn(null);
		borrowBookRepo.save(borrowedBook);

		jmsTemplate.convertAndSend(requestQueue, updateToTrueId);

		return new ResponseEntity<String>("The book was returned successfully", HttpStatus.OK);
	}

	@GetMapping("/borrowed-books/all")
	public ResponseEntity<List<BorrowBook>> getAllBorrowedBooks() {
		return new ResponseEntity<>(borrowBookRepo.findAll(), HttpStatus.OK);
	}

	@GetMapping("/successfully-returned-books")
	public ResponseEntity<List<BorrowBook>> successfullyReturnedBooks() {
		List<BorrowBook> returnedBooks = borrowBookRepo.findByReturnedBooks();
		return new ResponseEntity<>(returnedBooks, HttpStatus.OK);
	}

	@GetMapping("/currently-borrowed-books")
	public ResponseEntity<List<BorrowBook>> currentlyBorrowedBook() {
		List<BorrowBook> currentlyBorrowedBooks = borrowBookRepo
				.findByCurrentlyBorrowedBooks(new Timestamp(new Date().getTime()));

		return new ResponseEntity<>(currentlyBorrowedBooks, HttpStatus.OK);
	}

	@GetMapping("due-books")
	public ResponseEntity<List<BorrowBook>> dueBooks() {
		List<BorrowBook> dueBooks = borrowBookRepo.findByDueBooks(new Timestamp(new Date().getTime()));
		return new ResponseEntity<>(dueBooks, HttpStatus.OK);
	}

//	
//	
//	
//	
//	
//	

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
	public void getBook(MapMessage checkResponses) {
		this.checkResponses = checkResponses;
	}
}
