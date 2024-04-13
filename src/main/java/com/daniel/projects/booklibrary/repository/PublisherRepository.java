package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	Optional<Publisher> findOptionalByName(String name);

	Publisher findByName(String name);

	Optional <Publisher> findPublisherById(Long id);


	boolean existsByName(String name);
}
