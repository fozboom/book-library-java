package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.dto.AuthorResponseDTO;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorController {
	private final AuthorService service;
	private static final String SUCCESS_MESSAGE = "Success";

	@GetMapping("get")
	public List<AuthorResponseDTO> findAllAuthors() {

		return service.findAllAuthors();
	}

	@PostMapping("save")
	public ResponseEntity<String> addAuthor(@RequestBody Author author) {
		Optional<Author> savedBook = service.addAuthor(author);
		if (savedBook.isPresent()) {
			return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Book with this title already exists", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("find")
	public ResponseEntity<AuthorResponseDTO> findByName (@RequestParam String name) {
		AuthorResponseDTO author = service.findByName(name);
		if(author == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(author);
	}

	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam Long id, @RequestParam String name) {
		boolean updated = service.updateAuthorName(id, name);
		if(updated) {
			return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>("Author to update not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/delete/{name}")
	public ResponseEntity<String> deleteBookByName (@PathVariable String name) {
		if(service.deleteAuthorByName(name)) {
			return new ResponseEntity<>(SUCCESS_MESSAGE,HttpStatus.OK);
		}
		return new ResponseEntity<>("Author not found",HttpStatus.NOT_FOUND);
	}
}