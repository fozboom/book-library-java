package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryBookDAO {
	public final List<Book> allBooks = new ArrayList<>();

	public List<Book> findAllBooks() {
		return allBooks;
	}

	public Book addBook(Book book) {
		allBooks.add(book);
		return book;
	}

	public Book findByName(String bookName) {
		return allBooks.stream().filter(name -> name.getTitle().equals(bookName)).findFirst().orElse(null);
	}

	public String deleteBook(String bookName) {
		var book = findByName(bookName);

		if (book != null) {
			allBooks.remove(book);
			return "The book has been successfully deleted";
		} else {
			return "Book not found";
		}
	}
}
