package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Book;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Book findByTitle(String title);

	Optional<Book> findById(Long id);

	void deleteAuthorById(Long id);

	@Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
	List<Book> findByAuthorName(@Param("authorName") String authorName);

	boolean existsByTitle(String title);
}
