package com.daniel.projects.booklibrary.repository;

import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	Publisher findByName(String name);

	boolean existsByName(String name);
}
