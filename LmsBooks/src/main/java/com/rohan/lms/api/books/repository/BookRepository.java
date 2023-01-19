package com.rohan.lms.api.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rohan.lms.api.books.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	List<Book> findByAuthorName(String author);
	
	List<Book> findByBookGenre(String genre);
	
	@Query("SELECT b.authorName FROM Book b WHERE b.isbn = ?1")
	List<String> findAuthorByIsbn(String isbn);
	
	@Query("SELECT b.bookGenre FROM Book b WHERE b.isbn = ?1")
	List<String> findGenreByIsbn(String isbn);
	
}
