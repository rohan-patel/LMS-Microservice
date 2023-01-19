package com.rohan.lms.api.books.communication;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.rohan.lms.api.books.model.Book;
import com.rohan.lms.api.books.repository.BookRepository;

@Component
public class Communication {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private BookRepository bookRepo;
	
	@Value("${springjms.requestQueue}")
	private String requestQueue;
	@Value("${springjms.replyQueue}")
	private String replyQueue;
	
	@JmsListener(destination = "${springjms.requestQueue}")
	public void receiveIsbnAndSendBook(String isbn) {
		System.out.println("From book: message received and sending the book");
		Optional<Book> optionalBook = bookRepo.findById(isbn);
		if (optionalBook.isPresent()) {
			
			List<String> author = bookRepo.findAuthorByIsbn(isbn);
			List<String> genres = bookRepo.findGenreByIsbn(isbn);
			Book book = optionalBook.get();

			Book book2 = new Book(isbn, book.getBookName(), genres, author, book.getYearOfPublication(), book.isBookAvailability(),
					book.getAddedAt(), book.getUpdatedAt());
			
			jmsTemplate.convertAndSend(replyQueue, book2);
		}
	}
}
