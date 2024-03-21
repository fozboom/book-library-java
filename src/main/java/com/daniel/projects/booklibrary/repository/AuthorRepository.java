package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AuthorRepository extends JpaRepository<Author, Long> {
	Author findByName(String name);

	boolean existsByName(String name);

	@Query(value = "SELECT * FROM author "
			+ "WHERE name = :name", nativeQuery = true)
	Author findAuthorByName(@Param("name") String name);

}
