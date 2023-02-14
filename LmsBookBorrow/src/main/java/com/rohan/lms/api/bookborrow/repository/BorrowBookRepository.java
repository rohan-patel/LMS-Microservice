package com.rohan.lms.api.bookborrow.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rohan.lms.api.bookborrow.model.Book;
import com.rohan.lms.api.bookborrow.model.BorrowBook;

public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {

	Optional<BorrowBook> findByIsbn(String isbn);
	
	@Query("SELECT b FROM BorrowBook b WHERE b.bookReturned = true")
	List<BorrowBook> findByReturnedBooks();
	
	@Query("SELECT b FROM BorrowBook b WHERE b.bookReturned = null AND b.toBeReturnedOn > ?1")
	List<BorrowBook> findByCurrentlyBorrowedBooks(Timestamp currentTimestamp);
	
	@Query("SELECT b FROM BorrowBook b WHERE b.bookReturned = null AND b.toBeReturnedOn < ?1")
	List<BorrowBook> findByDueBooks(Timestamp currentTimestamp);

	@Query("SELECT b FROM BorrowBook b WHERE b.bookReturned = true AND b.toBeReturnedOn > ?1")
	List<BorrowBook> findByDueBooks2(Timestamp currentTimestamp2);
	
}
