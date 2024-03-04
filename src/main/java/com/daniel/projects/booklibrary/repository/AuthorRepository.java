package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	Author findByName(String name);
	int deleteByName(String name);

	boolean existsByName(String name);
	Optional<Author> findById (Long id);
}