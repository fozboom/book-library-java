package com.daniel.projects.booklibrary.controller;


import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Tag(name = "Books", description = "Interaction with books")
public class BookController {
	private final BookService service;
	private static final String SUCCESS_MESSAGE = "Success";

	@Operation(summary = "Get all books")
	@GetMapping("get")
	public List<BookResponseDTO> findAllBooks() {

		return service.findAllBooks();
	}

	@Operation(summary = "Save book")
	@PostMapping("save")
	public ResponseEntity<Book> addBook(@RequestBody final Book book) {
		Book savedBook = service.addBook(book);
		return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
	}

	@Operation(summary = "Get book by title")
	@GetMapping("findByTitle")
	public ResponseEntity<BookResponseDTO> findByTitle(@RequestParam final String bookName) {
		BookResponseDTO book = service.findByTitle(bookName);
		return ResponseEntity.ok(book);
	}

	@Operation(summary = "Get book by AuthorName")
	@GetMapping("findByAuthorName")
	public ResponseEntity<List<BookResponseDTO>> findByAuthorName(@RequestParam final String authorName) {

		return ResponseEntity.ok(service.findByAuthorName(authorName));
	}

	@Operation(summary = "Get book by id")
	@GetMapping("findById")
	public ResponseEntity<BookResponseDTO> findById(@RequestParam final Long id) {
		BookResponseDTO book = service.findById(id);
		return ResponseEntity.ok(book);
	}

	@Operation(summary = "Update book")
	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam final Double price, @RequestParam final String title) {
		service.updateBook(price, title);
		return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@Operation(summary = "Delete book by title")
	@DeleteMapping("delete/{bookName}")
	public ResponseEntity<String> deleteBookByTitle(@PathVariable final String bookName) {
		service.deleteBookByTitle(bookName);
		return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.OK);
	}
}
