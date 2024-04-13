package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Long> {

	boolean existsByName(String name);

	@Query(value = "SELECT * FROM author " + "WHERE name = :name", nativeQuery = true)
	Optional<Author> findAuthorByName(@Param("name") String name);
	Optional<Author> findAuthorById(Long id);


}
