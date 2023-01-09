package com.rohan.lms.api.books.model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

	@Id
	@Length(min=12, max=13)
	private String isbn;
	private String bookName;
	
	@ElementCollection
	private List<String> bookGenre;
	
	@ElementCollection
	private List<String> authorName;
	
	private Long yearOfPublication;
	private boolean bookAvailability;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd H:mm:ss")
	private Timestamp addedAt;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd H:mm:ss")
	private Timestamp updatedAt;

	public Book() {
		super();
	}

	public Book(@Length(min = 12, max = 13) String isbn, String bookName, List<String> bookGenre,
			List<String> authorName, Long yearOfPublication, boolean bookAvailability, Timestamp addedAt,
			Timestamp updatedAt) {
		super();
		this.isbn = isbn;
		this.bookName = bookName;
		this.bookGenre = bookGenre;
		this.authorName = authorName;
		this.yearOfPublication = yearOfPublication;
		this.bookAvailability = bookAvailability;
		this.addedAt = addedAt;
		this.updatedAt = updatedAt;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public List<String> getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(List<String> bookGenre) {
		this.bookGenre = bookGenre;
	}

	public List<String> getAuthorName() {
		return authorName;
	}

	public void setAuthorName(List<String> authorName) {
		this.authorName = authorName;
	}

	public Long getYearOfPublication() {
		return yearOfPublication;
	}

	public void setYearOfPublication(Long yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}

	public boolean isBookAvailability() {
		return bookAvailability;
	}

	public void setBookAvailability(boolean bookAvailability) {
		this.bookAvailability = bookAvailability;
	}

	public Timestamp getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(Timestamp addedAt) {
		this.addedAt = addedAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
}
