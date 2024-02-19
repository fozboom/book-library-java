package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class BookServiceImplementation implements BookService {

	private final BookRepository repository;

	public List<Book> findAllBooks() {
		return repository.findAll();
	}


	public Book addBook(Book book) {
		return repository.save(book);
	}


	public Book findByName(String bookName) {
		return repository.findByTitle(bookName);
	}


	public String deleteBook(String bookName) {
		repository.deleteByTitle(bookName);
		return "Good";
	}
}
