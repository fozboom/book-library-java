package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;



@CrossOrigin(value = "http://localhost:3000")
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
	public ResponseEntity<Author> addAuthor(@Valid @RequestBody final AuthorNameDTO authorName) {
		Author author = service.addAuthor(authorName);
		return new ResponseEntity<>(author, HttpStatus.CREATED);
	}

	@Operation(summary = "Save collection of authors")
	@PostMapping("saveCollection")
	public ResponseEntity<List<Author>> addAuthors(@Valid @RequestBody final List<AuthorNameDTO> authorNames) {
		List<Author> savedAuthors = service.addAuthors(authorNames);
		return new ResponseEntity<>(savedAuthors, HttpStatus.CREATED);
	}

	@Operation(summary = "Get author by name")
	@GetMapping("find")
	public ResponseEntity<AuthorResponseDTO> findByName(@Valid @RequestParam final String name) {
		AuthorResponseDTO author = service.findByName(name);
		return ResponseEntity.ok(author);
	}

	@Operation(summary = "Get author by id")
	@GetMapping("findById")
	public ResponseEntity<AuthorResponseDTO> findAuthorById(@RequestParam final Long id) {
		AuthorResponseDTO author = service.findAuthorById(id);
		return ResponseEntity.ok(author);
	}

	@Operation(summary = "Update author")
	@PutMapping("update")
	ResponseEntity<String> updateAuthor(@RequestParam final Long id, @Valid @RequestParam final String name) {
		service.updateAuthorName(id, name);
		return new ResponseEntity<>("Author updated successfully", HttpStatus.OK);
	}

	@Operation(summary = "Delete author by id")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteBookById(@Valid @PathVariable final Long id) {
		service.deleteAuthorById(id);
		return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
	}

}
