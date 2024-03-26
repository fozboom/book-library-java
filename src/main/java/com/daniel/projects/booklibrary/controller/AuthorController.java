package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Fields;
import com.daniel.projects.booklibrary.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
@Tag(name = "Authors", description = "Interaction with authors")
public class AuthorController {
	private final AuthorService service;

	@Operation(summary = "Get all authors")
	@GetMapping("get")
	public List<AuthorResponseDTO> findAllAuthors() {

		return service.findAllAuthors();
	}

	@Operation(summary = "Save author")
	@PostMapping("save")
	public ResponseEntity<String> addAuthor(@RequestBody final Author author) {
		Optional<Author> savedBook = service.addAuthor(author);
		if (savedBook.isPresent()) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Book with this title already exists", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Get author by name")
	@GetMapping("find")
	public ResponseEntity<AuthorResponseDTO> findByName(@RequestParam final String name) {
		AuthorResponseDTO author = service.findByName(name);
		if (author == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(author);
	}

	@Operation(summary = "Get author by id")
	@GetMapping("findById")
	public ResponseEntity<AuthorResponseDTO> findAuthorById(@RequestParam final Long id) {
		AuthorResponseDTO author = service.findAuthorById(id);
		if (author == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(author);
	}

	@Operation(summary = "Update author")
	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam final Long id, @RequestParam final String name) {
		boolean updated = service.updateAuthorName(id, name);
		if (updated) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>("Author to update not found", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Delete author by name")
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<String> deleteBookByName(@PathVariable final String name) {
		if (service.deleteAuthorByName(name)) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
	}
}
