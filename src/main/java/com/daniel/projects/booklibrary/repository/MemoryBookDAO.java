package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryBookDAO
{
	public final List<Book> BOOKS = new ArrayList<>();

	public List<Book> findAllBooks()
	{
		return BOOKS;
	}

	public Book addBook(Book book)
	{
		BOOKS.add(book);
		return book;
	}

	public Book findByName(String bookName)
	{
		return BOOKS.stream()
				.filter(name -> name.getName().equals(bookName))
				.findFirst().orElse(null);
	}

	public String deleteBook(String bookName)
	{
		var book = findByName(bookName);

		if (book != null)
		{
			BOOKS.remove(book);
			return "The book has been successfully deleted";
		}
		else
		{
			return "Book not found";
		}
	}
}
