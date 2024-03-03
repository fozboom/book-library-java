package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
	private final AuthorRepository repository;

	public List<Author> findAllAuthors() {
		return repository.findAll();
	}

	public Author addAuthor(Author author) {
		return repository.save(author);
	}

	public Author findByName(String name) {
		return repository.findByName(name);
	}

	@Transactional
	public String deleteAuthor(String name) {
		int res = repository.deleteByName(name);
		if (res > 0) {
			return "Author deleted successfully";
		}
		return "No author found with the given name";
	}
}
