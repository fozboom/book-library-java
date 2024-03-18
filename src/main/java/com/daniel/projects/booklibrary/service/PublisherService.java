package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTOMapper;
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
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PublisherService {

	private final PublisherRepository publisherRepository;
	private final BookRepository bookRepository;
	private final PublisherResponseDTOMapper publisherMapper;
	private final CacheService cacheService;
	private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

	public List<PublisherResponseDTO> findAllPublishers() {

		return publisherRepository.findAll().stream().map(publisherMapper).toList();
	}

	public Optional<Publisher> addPublisher(PublisherSaveDTO publisher) {
		if (publisherRepository.existsByName(publisher.getName())) {
			return Optional.empty();
		}
		Publisher newPublisher = new Publisher();
		newPublisher.setName(publisher.getName());
		newPublisher.setAddress(publisher.getAddress());
		cacheService.addPublisher(newPublisher);
		return Optional.of(publisherRepository.save(newPublisher));
	}


	public PublisherResponseDTO findByName(String title) {
		Publisher publisher = publisherRepository.findByName(title);

		if (publisher == null) {
			return null;
		}

		return publisherMapper.apply(publisher);
	}

	public PublisherResponseDTO findPublisherById(Long id) {
		Publisher publisher = cacheService.getPublisher(id);

		if (publisher == null) {
			Optional<Publisher> optionalPublisher = publisherRepository.findById(id);

			if (optionalPublisher.isEmpty()) {
				return null;
			}

			Publisher retrievedPublisher = optionalPublisher.get();

			cacheService.addPublisher(retrievedPublisher);
			logger.info("Publisher retrieved from repository and added to cache");
			return publisherMapper.apply(retrievedPublisher);
		} else {
			logger.info("Publisher retrieved from cache");
		}

		return publisherMapper.apply(publisher);
	}


	public boolean updatePublisherName(Long id, String newName) {
		Optional<Publisher> existingAuthorOptional = publisherRepository.findById(id);
		if (existingAuthorOptional.isEmpty()) {
			return false;
		}

		Publisher existingPublisher = existingAuthorOptional.get();
		existingPublisher.setName(newName);
		publisherRepository.save(existingPublisher);
		cacheService.updatePublisher(existingPublisher);
		return true;
	}

	public boolean deletePublisherByName(String name) {
		Publisher publisher = publisherRepository.findByName(name);

		if (publisher != null) {
			for (Book book : publisher.getBooks()) {
				book.setPublisher(null);
				bookRepository.save(book);
			}
			publisherRepository.delete(publisher);
			cacheService.removePublisher(publisher.getId());
			return true;
		}
		return false;
	}

}