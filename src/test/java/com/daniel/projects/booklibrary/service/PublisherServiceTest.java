package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.exception.ResourceAlreadyExistsException;
import com.daniel.projects.booklibrary.exception.ResourceNotFoundException;
import com.daniel.projects.booklibrary.mapper.PublisherResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.BookRepository;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
	@InjectMocks
	PublisherService publisherService;
	@Mock
	PublisherRepository publisherRepository;
	@Mock
	BookRepository bookRepository;

	@Mock
	CacheService cacheService;

	@Mock
	PublisherResponseDTOMapper publisherMapper;

	List<Publisher> publishers;

	Publisher publisher;
	PublisherResponseDTO publisherResponseDTO;
	PublisherSaveDTO publisherSaveDTO;

	@BeforeEach
	void setUp() {
		publisher = new Publisher();
		publisher.setId(1L);
		publisher.setName("Publisher Name");
		publisher.setAddress("Publisher Address");

		publisherResponseDTO = new PublisherResponseDTO();
		publisherResponseDTO.setId(publisher.getId());
		publisherResponseDTO.setName(publisher.getName());

		publisherSaveDTO = new PublisherSaveDTO();
		publisherSaveDTO.setName(publisher.getName());


		publishers = new ArrayList<>();
		publishers.add(publisher);
	}

	@Test
	void testFindAllPublishers_Empty() {
		when(publisherRepository.findAll()).thenReturn(new ArrayList<>());
		assertEquals(0, publisherService.findAllPublishers().size());
	}

	@Test
	void testFindAllPublishers_NotEmpty() {
		when(publisherRepository.findAll()).thenReturn(publishers);
		when(publisherMapper.apply(publisher)).thenReturn(publisherResponseDTO);

		List<PublisherResponseDTO> result = publisherService.findAllPublishers();

		assertEquals(1, result.size());
		assertEquals(publisherResponseDTO, result.get(0));
	}

	@Test
	void testAddPublisher_NonExistingName() {
		when(publisherRepository.existsByName(publisherSaveDTO.getName())).thenReturn(false);
		when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

		assertEquals(publisher, publisherService.addPublisher(publisherSaveDTO));
	}

	@Test
	void testAddPublisher_ExistingName() {
		when(publisherRepository.existsByName(publisherSaveDTO.getName())).thenReturn(true);
		assertThrows(ResourceAlreadyExistsException.class, () -> {
			publisherService.addPublisher(publisherSaveDTO);
		});
	}

	@Test
	void addPublishers_NotExistingNames() {
		when(publisherRepository.existsByName(publisherSaveDTO.getName())).thenReturn(false);
		when(publisherRepository.saveAll(anyList())).thenReturn(publishers);

		assertEquals(publishers, publisherService.addPublishers(List.of(publisherSaveDTO)));
	}

	@Test
	void addPublishers_ExistingNames() {
		when(publisherRepository.existsByName(publisherSaveDTO.getName())).thenReturn(true);
		assertEquals(new ArrayList<Publisher>(), publisherService.addPublishers(List.of(publisherSaveDTO)));
	}

	@Test
	void testFindByName_Founded() {
		when(publisherRepository.findOptionalByName(publisher.getName())).thenReturn(java.util.Optional.of(publisher));
		when(publisherMapper.apply(publisher)).thenReturn(publisherResponseDTO);

		assertEquals(publisherResponseDTO, publisherService.findByName(publisher.getName()));
	}

	@Test
	void testFindByName_NotFounded() {
		String name = publisher.getName();
		when(publisherRepository.findOptionalByName(publisher.getName())).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			publisherService.findByName(name);
		});
	}

	@Test
	void testFindPublisherByIdFromCache() {
		when(cacheService.getPublisher(publisher.getId())).thenReturn(publisher);
		when(publisherMapper.apply(publisher)).thenReturn(publisherResponseDTO);

		assertEquals(publisherResponseDTO, publisherService.findPublisherById(publisher.getId()));
	}

	@Test
	void testFindPublisherByIdFromRepository() {
		when(cacheService.getPublisher(publisher.getId())).thenReturn(null);
		when(publisherRepository.findById(publisher.getId())).thenReturn(java.util.Optional.of(publisher));
		when(publisherMapper.apply(publisher)).thenReturn(publisherResponseDTO);

		assertEquals(publisherResponseDTO, publisherService.findPublisherById(publisher.getId()));
	}

	@Test
	void testUpdatePublisherName_Founded() {
		Long id = publisher.getId();
		String newName = "New Publisher Name";
		when(publisherRepository.findById(publisher.getId())).thenReturn(java.util.Optional.of(publisher));

		assertDoesNotThrow(() -> {
			publisherService.updatePublisherName(id, newName);
		});
	}

	@Test
	void testUpdatePublisherName_NotFounded() {
		String newName = "New Publisher Name";
		Long id = publisher.getId();
		when(publisherRepository.findById(publisher.getId())).thenReturn(java.util.Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			publisherService.updatePublisherName(id, newName);
		});
	}

	@Test
	void testDeletePublisherById_Founded() {
		Book book = new Book();
		book.setPublisher(publisher);
		book.setId(1L);
		book.setTitle("Book title");

		when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		publisher.getBooks().add(book);

		assertDoesNotThrow(() -> {
			publisherService.deletePublisherById(publisher.getId());
		});

		verify(bookRepository).save(book);
		verify(publisherRepository).delete(publisher);
		verify(cacheService).removePublisher(publisher.getId());
	}







}
