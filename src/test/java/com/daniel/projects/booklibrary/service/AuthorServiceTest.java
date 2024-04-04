
package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.exception.ResourceAlreadyExistsException;
import com.daniel.projects.booklibrary.exception.ResourceNotFoundException;
import com.daniel.projects.booklibrary.mapper.AuthorResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import com.daniel.projects.booklibrary.repository.BookRepository;
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
class AuthorServiceTest {
	@InjectMocks
	private AuthorService authorService;
	@Mock
	private AuthorRepository authorRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private AuthorResponseDTOMapper authorMapper;
	@Mock
	private CacheService cacheService;
	private Author author;
	private AuthorNameDTO authorNameDTO;
	private List<AuthorNameDTO> authorNames;

	@BeforeEach
	void setUp() {
		author = new Author();
		author.setId(1L);
		author.setName("Author Name");

		authorNameDTO = new AuthorNameDTO();
		authorNameDTO.setName("Author Name");

		authorNames = new ArrayList<>();
		authorNames.add(authorNameDTO);
	}

	@Test
	void testFindAllAuthors_Empty() {
		when(authorRepository.findAll()).thenReturn(new ArrayList<>());

		List<AuthorResponseDTO> result = authorService.findAllAuthors();

		assertEquals(0, result.size());
	}
	@Test
	void testFindAllAuthors_NotEmpty() {
		List<Author> authors = new ArrayList<>();
		authors.add(author);

		when(authorRepository.findAll()).thenReturn(authors);
		when(authorMapper.apply(author)).thenReturn(new AuthorResponseDTO());

		List<AuthorResponseDTO> result = authorService.findAllAuthors();

		assertEquals(1, result.size());
	}

	@Test
	void testAddAuthor_NonExistingName() {
		when(authorRepository.existsByName(authorNameDTO.getName())).thenReturn(false);
		when(authorRepository.save(any(Author.class))).thenReturn(author);

		Author result = authorService.addAuthor(authorNameDTO);

		assertEquals(authorNameDTO.getName(), result.getName());
	}

	@Test
	void testAddAuthor_ExistingName() {
		when(authorRepository.existsByName(authorNameDTO.getName())).thenReturn(true);

		assertThrows(ResourceAlreadyExistsException.class, () -> {
			authorService.addAuthor(authorNameDTO);
		});
	}

	@Test
	void testAddAuthors_NotExistingNames() {
		when(authorRepository.existsByName(authorNameDTO.getName())).thenReturn(false);
		when(authorRepository.saveAll(anyList())).thenReturn(new ArrayList<>(List.of(author)));

		List<Author> result = authorService.addAuthors(authorNames);

		assertEquals(1, result.size());
		assertEquals(authorNameDTO.getName(), result.get(0).getName());
	}
	@Test
	void testAddAuthors_ExistingNames() {
		when(authorRepository.existsByName(authorNameDTO.getName())).thenReturn(true);

		List<Author> result = authorService.addAuthors(authorNames);

		assertEquals(0, result.size());
	}
	@Test
	void findByName_Founded() {
		when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.of(author));
		when(authorMapper.apply(author)).thenReturn(new AuthorResponseDTO());

		AuthorResponseDTO result = authorService.findByName(author.getName());

		assertNotNull(result);
	}

	@Test
	void findByName_NotFounded() {
		String authorName = author.getName();
		when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			authorService.findByName(authorName);
		});
	}

	@Test
	void testUpdateAuthorName_Founded() {
		String newName = "New Name";

		when(authorRepository.findAuthorById(author.getId())).thenReturn(Optional.of(author));

		assertDoesNotThrow(() -> {
			authorService.updateAuthorName(author.getId(), newName);
		});
	}
	@Test
	void testUpdateAuthorName_NotFounded() {
		String newName = "New Name";
		Long id = author.getId();

		when(authorRepository.findAuthorById(author.getId())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			authorService.updateAuthorName(id, newName);
		});
	}

	@Test
	public void testFindAuthorByIdFromCache() {
		when(cacheService.getAuthor(author.getId())).thenReturn(author);
		when(authorMapper.apply(author)).thenReturn(new AuthorResponseDTO());

		AuthorResponseDTO result = authorService.findAuthorById(author.getId());

		assertNotNull(result);
		verify(authorRepository, never()).findById(anyLong());
	}
	@Test
	void testFindAuthorByIdFromRepository() {
		when(cacheService.getAuthor(author.getId())).thenReturn(null);
		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		when(authorMapper.apply(author)).thenReturn(new AuthorResponseDTO());

		AuthorResponseDTO result = authorService.findAuthorById(author.getId());

		assertNotNull(result);
		verify(cacheService).addAuthor(author);
	}

	@Test
	void testDeleteAuthorByName() {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Book Title");
		book.setAuthors(new ArrayList<>(List.of(author)));

		when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.of(author));
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		author.getBooks().add(book);

		assertDoesNotThrow(() -> {
			authorService.deleteAuthorByName(author.getName());
		});

		verify(bookRepository).save(any(Book.class));
		verify(authorRepository).delete(author);
		verify(cacheService).removeAuthor(author.getId());
	}
}