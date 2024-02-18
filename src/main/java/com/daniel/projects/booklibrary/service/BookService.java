package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.model.Book;

import java.util.List;


public interface BookService
{
	public List<Book> findAllBooks();

	public Book addBook(Book book);

	public Book findByName (String bookName);

	public String deleteBook (String bookName);

}
