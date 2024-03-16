package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Book findByTitle(String title);
	void deleteAuthorById(Long id);
	@Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
	List<Book> findByAuthorName(@Param("authorName") String authorName);

	boolean existsByTitle(String title);
}
