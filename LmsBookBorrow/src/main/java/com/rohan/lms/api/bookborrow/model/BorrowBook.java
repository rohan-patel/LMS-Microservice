package com.rohan.lms.api.bookborrow.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "borrow_book_seq", allocationSize = 1, initialValue = 1)
public class BorrowBook {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrow_book_seq")
	private Long borrowId;

	private String isbn;
	private Long memberId;
	private Long employeeId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp borrowedOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp toBeReturnedOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp returnedOn;

	private Boolean bookReturned;

	public BorrowBook() {
		super();
	}

	public BorrowBook(String isbn, Long memberId, Long employeeId) {
		super();
		this.isbn = isbn;
		this.memberId = memberId;
		this.employeeId = employeeId;
	}

	public BorrowBook(String isbn, Long memberId, Long employeeId, Timestamp borrowedOn, Timestamp toBeReturnedOn,
			Timestamp returnedOn, Boolean bookReturned) {
		super();
		this.isbn = isbn;
		this.memberId = memberId;
		this.employeeId = employeeId;
		this.borrowedOn = borrowedOn;
		this.toBeReturnedOn = toBeReturnedOn;
		this.returnedOn = returnedOn;
		this.bookReturned = bookReturned;
	}

	public BorrowBook(String isbn, Long memberId, Long employeeId, Timestamp borrowedOn, Timestamp toBeReturnedOn) {
		super();
		this.isbn = isbn;
		this.memberId = memberId;
		this.employeeId = employeeId;
		this.borrowedOn = borrowedOn;
		this.toBeReturnedOn = toBeReturnedOn;
	}

	public Long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(Long borrowId) {
		this.borrowId = borrowId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Timestamp getBorrowedOn() {
		return borrowedOn;
	}

	public void setBorrowedOn(Timestamp borrowedOn) {
		this.borrowedOn = borrowedOn;
	}

	public Timestamp getToBeReturnedOn() {
		return toBeReturnedOn;
	}

	public void setToBeReturnedOn(Timestamp toBeReturnedOn) {
		this.toBeReturnedOn = toBeReturnedOn;
	}

	public Timestamp getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(Timestamp returnedOn) {
		this.returnedOn = returnedOn;
	}

	public Boolean getBookReturned() {
		return bookReturned;
	}

	public void setBookReturned(Boolean bookReturned) {
		this.bookReturned = bookReturned;
	}

}
