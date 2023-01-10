package com.rohan.lms.api.bookborrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohan.lms.api.bookborrow.model.BorrowBook;

public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {

}
