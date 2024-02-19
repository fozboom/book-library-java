package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {

	void deleteByTitle(String name);

	Book findByTitle(String title);
}
