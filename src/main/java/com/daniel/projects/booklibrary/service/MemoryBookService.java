package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.MemoryBookDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class MemoryBookService implements BookService
{
	private MemoryBookDAO repository;
	public List<Book> findAllBooks()
	{
		return repository.findAllBooks();
	}

	public Book addBook(Book book)
	{
		return repository.addBook(book);
	}

	public Book findByName(String bookName)
	{
		return repository.findByName(bookName);
	}

	public String deleteBook(String bookName)
	{
		return repository.deleteBook(bookName);
	}
}
