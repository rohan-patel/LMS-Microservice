package com.rohan.lms.api.books.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.books.model.Book;
import com.rohan.lms.api.books.model.Error;
import com.rohan.lms.api.books.repository.BookRepository;
import com.rohan.lms.api.books.utilities.Utilities;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * GET - get all books 
	 * GET - get a book by isbn 
	 * GET - get a book by Genre
	 * GET - get a book by Author name
	 * POST - add a book to library 
	 * PUT - update a book details by book id (partial update - isbn is necessary)
	 * DELETE - remove a book from library
	 */

	@GetMapping("/all")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> bookList = new ArrayList<>();
		bookRepo.findAll().forEach(bookList::add);

		return new ResponseEntity<>(bookList, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Book> getOneBook(@RequestParam String isbn) {

		Optional<Book> optionalBook = bookRepo.findById(isbn);
		if (optionalBook.isPresent() != true) {
			String message = "Book with isbn " + isbn + " not found in the database";
			return Utilities.buildRestTemplate(message, "entity-not-found");
		}

		Book book = optionalBook.get();

		return new ResponseEntity<>(book, HttpStatus.OK);
	}
	
	@GetMapping("/genre")
	public ResponseEntity<List<Book>> getBooksByGenre(@RequestParam String genre) {
		
		List<Book> books = bookRepo.findByBookGenre(genre);
		
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/author")
	public ResponseEntity<List<Book>> getBookByAuthor(@RequestParam String author) {

		List<Book> book = bookRepo.findByAuthorName(author);

		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {

		if (bookRepo.findById(book.getIsbn()).isPresent() == true) {
			String message = "Book with isbn " + book.getIsbn() + " already exists in the database";
			return Utilities.buildRestTemplate(message, "entity-already-exists");
		}

		Book _book = bookRepo.save(new Book(book.getIsbn(), book.getBookName(), book.getBookGenre(),
				book.getAuthorName(), book.getYearOfPublication(), true, new Timestamp(new Date().getTime()), null));

		return new ResponseEntity<>(_book, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Book> updateBook(@RequestBody Map<String, Object> payload) {

		if (payload.get("isbn") == null) {
			String message = "Please provide a valid ISBN of the book present in the database";
			return Utilities.buildRestTemplate(message, "insufficient-data");
		}

		Optional<Book> optionalBook = bookRepo.findById((String) payload.get("isbn"));
		Book book = optionalBook.get();

		if (book == null) {
			String message = "Book with isbn " + payload.get("isbn") + " not found in the database";
			return Utilities.buildRestTemplate(message, "entity-not-found");
		}

		List<String> authorNames = (List<String>) payload.get("authorName");
		System.out.println(authorNames);

		for (Entry<String, Object> entry : payload.entrySet()) {
			if (entry.getKey() != "isbn") {
				switch (entry.getKey()) {
				case "bookName":
					book.setBookName((String) entry.getValue());
					break;
				case "bookGenre":
					book.setBookGenre((List<String>) entry.getValue());
					break;
				case "authorName":
					book.setAuthorName((List<String>) entry.getValue());
					break;
				case "yearOfPublication":
					book.setYearOfPublication(Long.parseLong((String) entry.getValue()));
					break;
				}
			}
		}

		book.setUpdatedAt(new Timestamp(new Date().getTime()));

		Book _book = bookRepo.save(book);

		return new ResponseEntity<Book>(_book, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestParam String isbn) {

		if (bookRepo.findById(isbn).isPresent() != true) {
			String message = "Book with isbn " + isbn + " not found in the database";
			return Utilities.buildRestTemplate(message, "entity-not-found");
		}

		bookRepo.deleteById(isbn);

		return new ResponseEntity<>("The book was deleted successfully", HttpStatus.OK);
	}


}
