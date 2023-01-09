package com.rohan.lms.api.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohan.lms.api.books.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	List<Book> findByAuthorName(String author);
	
	List<Book> findByBookGenre(String genre);
	
}
