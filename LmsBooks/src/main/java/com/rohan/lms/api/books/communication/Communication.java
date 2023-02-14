package com.rohan.lms.api.books.communication;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.rohan.lms.api.books.model.Book;
import com.rohan.lms.api.books.repository.BookRepository;
import com.rohan.lms.api.books.utilities.Utilities;

import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.Session;

import com.rohan.lms.api.books.controllers.BookController;

@Component
public class Communication {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private BookRepository bookRepo;

	private String isbn;

	@Value("${springjms.requestQueue}")
	private String requestQueue;
	
	@Value("${springjms.replyQueue}")
	private String replyQueue;


	@JmsListener(destination = "${springjms.requestQueue}")
	public void receiveIsbnAndSendBook(String command) {
		
		System.out.println("From book: message received and sending the book");
		if (command.length() >= 10) {
			System.out.println("Setting ISBN");
			isbn = command;
		} else {
			switch (command) {
			case "1":
				System.out.println("In Case 1");
				if (bookRepo.findById(isbn).isPresent() != true) {
					jmsTemplate.send(replyQueue, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							MapMessage mapMessage = session.createMapMessage();
							mapMessage.setBoolean("entityNotFound", true);
							mapMessage.setInt("executedCheck", 1);
							return mapMessage;
						}
					});
				}
				else if (bookRepo.findById(isbn).get().isBookAvailability() == false) {
					jmsTemplate.send(replyQueue, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							MapMessage mapMessage = session.createMapMessage();
							mapMessage.setBoolean("bookUnavailable", true);
							mapMessage.setInt("executedCheck", 2);
							return mapMessage;
						}
					});
				} else {
					jmsTemplate.send(replyQueue, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							MapMessage mapMessage = session.createMapMessage();
							mapMessage.setInt("executedCheck", 0);
							return mapMessage;
						}
					});
					
				}
				
				System.out.println("Executed Case 1");
				break;
			case "2":
				Book book = bookRepo.findById(isbn).get();
				book.setBookAvailability(false);
				book.setUpdatedAt(new Timestamp(new Date().getTime()));
				bookRepo.save(book);
				System.out.println("Executed Case 2");
				break;
			case "3":
				Book book2 = bookRepo.findById(isbn).get();
				book2.setBookAvailability(true);
				book2.setUpdatedAt(new Timestamp(new Date().getTime()));
				bookRepo.save(book2);
				System.out.println("Executed Case 3");
				break;
			}
		}

		System.out.println("Executed from Book");

//		Optional<Book> optionalBook = bookRepo.findById(command);
//		if (optionalBook.isPresent()) {
//
//			List<String> author = bookRepo.findAuthorByIsbn(command);
//			List<String> genres = bookRepo.findGenreByIsbn(command);
//			Book book = optionalBook.get();
//
//			Book book2 = new Book(isbn, book.getBookName(), genres, author, book.getYearOfPublication(),
//					book.isBookAvailability(), book.getAddedAt(), book.getUpdatedAt());
//
//			jmsTemplate.convertAndSend(replyQueue, book2);
//		}

	}
}
