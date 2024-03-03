package com.daniel.projects.booklibrary.controller;


import com.daniel.projects.booklibrary.dto.BookDTO;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController
{

	private final BookService service;

	@GetMapping("get")
	public List<BookDTO> findAllBooks() {

		return service.findAllBooks();
	}


	@PostMapping("save")
	public Book addBook (@RequestBody Book book) {

		return service.addBook(book);
	}

	@GetMapping("find")
	public BookDTO findByName (@RequestParam String bookName) {

		return service.findByTitle(bookName);
	}

	@DeleteMapping("delete/{bookName}")
	public String deleteBook (@PathVariable String bookName) {

		return service.deleteBook(bookName);
	}
}


