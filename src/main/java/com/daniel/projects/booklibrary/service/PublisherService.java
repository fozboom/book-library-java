package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
	private final PublisherRepository publisherRepository;

	@Autowired
	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}

	public Publisher savePublisher(Publisher publisher) {
		return publisherRepository.save(publisher);
	}

	public Publisher getPublisher(Long id) {
		return publisherRepository.findById(id).orElse(null);
	}

	public List<Publisher> getAllPublishers() {
		return publisherRepository.findAll();
	}

	public void deletePublisher(Long id) {
		publisherRepository.deleteById(id);
	}
}
