package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTOMapper;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublisherService {

	private final PublisherRepository repository;
	private final PublisherResponseDTOMapper mapper;

	@Transactional
	public List<PublisherResponseDTO> findAllPublishers() {

		return repository.findAll().
				stream().map(mapper).toList();
	}

	@Transactional
	public Optional<Publisher> addPublisher(PublisherSaveDTO newPublisher) {
		if (repository.existsByName(newPublisher.getName())) {
			return Optional.empty();
		}
		Publisher publisher = new Publisher();
		publisher.setName(newPublisher.getName());
		publisher.setAddress(newPublisher.getAddress());
		return Optional.of(repository.save(publisher));
	}


	@Transactional
	public PublisherResponseDTO findByName(String title) {
		Publisher publisher = repository.findByName(title);

		if (publisher == null) {
			return null;
		}

		return mapper.apply(publisher);
	}

	@Transactional
	public boolean updatePublisherName(Long id, String newName) {
		Optional<Publisher> existingAuthorOptional = repository.findById(id);
		if (existingAuthorOptional.isEmpty()) {
			return false;
		}

		Publisher existingPublisher = existingAuthorOptional.get();
		existingPublisher.setName(newName);
		repository.save(existingPublisher);

		return true;
	}

	@Transactional
	public boolean deleteBookByTitle(String name) {

		Publisher publisher = repository.findByName(name);

		if (publisher != null) {
			repository.delete(publisher);
			return true;
		}
		return false;
	}

}