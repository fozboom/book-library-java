package com.daniel.projects.booklibrary.controller;


import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController
{
	private final BookService service;
	private static final String SUCCESS_MESSAGE = "Success";
	@GetMapping("get")
	public List<BookResponseDTO> findAllBooks() {

		return service.findAllBooks();
	}

	@PostMapping("save")
	public ResponseEntity<String> addBook(@RequestBody Book book) {
		Optional<Book> savedBook = service.addBook(book);
		if (savedBook.isPresent()) {
			return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Book with this title already exists", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("find")
	public ResponseEntity<BookResponseDTO> findByName (@RequestParam String bookName) {
		BookResponseDTO book = service.findByTitle(bookName);
		if(book == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(book);
	}

	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam Double price, @RequestParam String title) {
		boolean updated = service.updateBook(price, title);
		if(updated) {
            return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.OK);
        }
		return new ResponseEntity<>("Book to update not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("delete/{bookName}")
	public ResponseEntity<String> deleteBookByTitle (@PathVariable String bookName) {
		if(service.deleteBookByTitle(bookName)) {
			return new ResponseEntity<>(SUCCESS_MESSAGE,HttpStatus.OK);
		}
		return new ResponseEntity<>("Book not found",HttpStatus.NOT_FOUND);
	}
}


