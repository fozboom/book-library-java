package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.exception.ResourceAlreadyExistsException;
import com.daniel.projects.booklibrary.exception.ResourceNotFoundException;
import com.daniel.projects.booklibrary.mapper.PublisherResponseDTOMapper;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.BookRepository;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PublisherService {

	private final PublisherRepository publisherRepository;
	private final BookRepository bookRepository;
	private final PublisherResponseDTOMapper publisherMapper;
	private final CacheService cacheService;
	private static final String MESSAGE = "Publisher not found with ";
	private static final Logger LOGGER = LoggerFactory.getLogger(PublisherService.class);

	public List<PublisherResponseDTO> findAllPublishers() {

		return publisherRepository.findAll().stream().map(publisherMapper).toList();
	}

	public Publisher addPublisher(final PublisherSaveDTO publisher) {
		if (publisherRepository.existsByName(publisher.getName())) {
			throw new ResourceAlreadyExistsException("Publisher with this name already exists");
		}
		Publisher newPublisher = new Publisher();
		newPublisher.setName(publisher.getName());
		newPublisher.setAddress(publisher.getAddress());
		cacheService.addPublisher(newPublisher);
		return publisherRepository.save(newPublisher);
	}

	public List<Publisher> addPublishers(final List<PublisherSaveDTO> publishers) {
		List<Publisher> newPublishers = publishers.stream().filter(publisherSaveDTO -> !publisherRepository.existsByName(publisherSaveDTO.getName())).map(publisherSaveDTO -> {
			Publisher publisher = new Publisher();
			publisher.setName(publisherSaveDTO.getName());
			publisher.setAddress(publisherSaveDTO.getAddress());
			return publisher;
		}).toList();
		return publisherRepository.saveAll(newPublishers);
	}


	public PublisherResponseDTO findByName(final String title) {
		Publisher publisher = publisherRepository.findOptionalByName(title).orElseThrow(() -> new ResourceNotFoundException("Publisher not found with name: " + title));
		return publisherMapper.apply(publisher);
	}

	public PublisherResponseDTO findPublisherById(final Long id) {
		Publisher publisher = cacheService.getPublisher(id);

		if (publisher == null) {
			Publisher retrievedPublisher = publisherRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(MESSAGE + id));


			cacheService.addPublisher(retrievedPublisher);
			LOGGER.info("Publisher retrieved from " + "repository and added to cache");
			return publisherMapper.apply(retrievedPublisher);
		} else {
			LOGGER.info("Publisher retrieved from cache");
		}

		return publisherMapper.apply(publisher);
	}


	public void updatePublisherName(final Long id, final String newName) {
		Publisher existingPublisher = publisherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));

		existingPublisher.setName(newName);
		publisherRepository.save(existingPublisher);
		cacheService.updatePublisher(existingPublisher);
	}

	public void deletePublisherById(final Long id) {
		Publisher publisher = publisherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(MESSAGE + id));
		for (Book book : publisher.getBooks()) {
			book.setPublisher(null);
			bookRepository.save(book);
		}
		publisherRepository.delete(publisher);
		cacheService.removePublisher(publisher.getId());

	}
}