package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorController {
	private final AuthorService service;

	@GetMapping("get")
	public List<Author> findAllAuthors() {

		return service.findAllAuthors();
	}

	@PostMapping("save")
	public Author addAuthor(@RequestBody Author author) {

		return service.addAuthor(author);
	}

	@GetMapping("/find")
	public Author findByName(@RequestParam String name) {

		return service.findByName(name);
	}

	@DeleteMapping("/delete/{name}")
	public String deleteAuthor(@PathVariable String name) {

		return service.deleteAuthor(name);
	}
}