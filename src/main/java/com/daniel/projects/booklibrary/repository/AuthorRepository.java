package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	Author findByName(String name);
	int deleteByName(String name);
}