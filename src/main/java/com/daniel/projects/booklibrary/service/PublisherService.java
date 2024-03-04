package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.PublisherDTO;
import com.daniel.projects.booklibrary.dto.PublisherDTOMapper;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PublisherService {

	private final PublisherRepository repository;
	private final PublisherDTOMapper mapper;

	public List<PublisherDTO> findAllPublishers() {

		return repository.findAll().
				stream().map(mapper).toList();
	}

	public Publisher addPublisher(Publisher publisher) {

		return repository.save(publisher);
	}

	public Publisher findByName(String publisherName) {

		return repository.findByName(publisherName);
	}

	public String deletePublisher(String publisherName) {
		Publisher publisher = findByName(publisherName);
		if (publisher != null) {
			repository.delete(publisher);
			return "Publisher deleted successfully";
		}
		return "Publisher not found";
	}
}