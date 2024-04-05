package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.exception.ResourceAlreadyExistsException;
import com.daniel.projects.booklibrary.exception.ResourceNotFoundException;
import com.daniel.projects.booklibrary.mapper.BookResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@Mock
	private CacheService cacheService;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private AuthorRepository authorRepository;

	@Mock
	private PublisherRepository publisherRepository;

	@Mock
	private BookResponseDTOMapper mapper;

	@InjectMocks
	private BookService bookService;

	private Book book;
	private BookResponseDTO bookResponseDTO;

	@BeforeEach
	void setUp() {
		book = new Book();
		book.setTitle("Test Book");
		book.setId(1L);

		Publisher publisher = new Publisher();
		publisher.setName("Test Publisher");
		publisher.setId(1L);
		book.setPublisher(publisher);


		bookResponseDTO = new BookResponseDTO();
		bookResponseDTO.setTitle("Test Book");
	}

	@Test
	void testFindAllBooks_Empty() {
		when(bookRepository.findAllBooks()).thenReturn(new ArrayList<>());
		List<BookResponseDTO> books = bookService.findAllBooks();
		assertEquals(0, books.size());

	}

	@Test
	void testFindAllBooks_NotEmpty() {
		List<Book> books = new ArrayList<>();
		books.add(book);

		when(bookRepository.findAllBooks()).thenReturn(books);
		when(mapper.apply(book)).thenReturn(bookResponseDTO);

		List<BookResponseDTO> result = bookService.findAllBooks();
		assertEquals(1, result.size());
	}

	@Test
	void testAddBook_NotExistingName() {
		when(bookRepository.existsByTitle(book.getTitle())).thenReturn(false);
		when(bookRepository.save(book)).thenReturn(book);
		when(publisherRepository.save(any(Publisher.class))).thenReturn(book.getPublisher());

		Book savedBook = bookService.addBook(book);
		assertEquals(book, savedBook);
	}

	@Test
	void testAddBook_ExistingName() {
		when(bookRepository.existsByTitle(book.getTitle())).thenReturn(true);
		assertThrows(ResourceAlreadyExistsException.class, () -> bookService.addBook(book));
	}

	@Test
	void testFindByTitle_ExistingTitle() {
		when(bookRepository.findByTitle(book.getTitle())).thenReturn(java.util.Optional.of(book));
		when(mapper.apply(book)).thenReturn(bookResponseDTO);

		BookResponseDTO result = bookService.findByTitle(book.getTitle());
		assertEquals(bookResponseDTO, result);
	}

	@Test
	void testFindByTitle_NotExistingTitle() {
		when(bookRepository.findByTitle(book.getTitle())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> bookService.findByTitle(book.getTitle()));
	}

	@Test
	void testFindByIdFromCache() {
		when(cacheService.getBook(book.getId())).thenReturn(book);
		when(mapper.apply(book)).thenReturn(bookResponseDTO);
		BookResponseDTO result = bookService.findById(book.getId());
		assertEquals(bookResponseDTO, result);
	}

	@Test
	void testFindByIdFromRepository() {
		when(cacheService.getBook(book.getId())).thenReturn(null);
		when(bookRepository.findBookById(book.getId())).thenReturn(Optional.of(book));
		when(mapper.apply(book)).thenReturn(bookResponseDTO);
		BookResponseDTO result = bookService.findById(book.getId());
		assertEquals(bookResponseDTO, result);
	}

	@Test
	void testUpdateBook_ExistingTitle() {
		String title = "Test Book";
		Double price = 19.99;
		book.setTitle(title);
		book.setPrice(9.99);

		when(bookRepository.findByTitle(title)).thenReturn(Optional.of(book));

		bookService.updateBook(price, title);

		verify(bookRepository, times(1)).findByTitle(title);
		verify(bookRepository, times(1)).save(book);
		verify(cacheService, times(1)).addBook(book);
		assertEquals(price, book.getPrice());
	}

	@Test
	void testUpdateBook_NotExistingTitle() {
		String title = "Nonexistent Book";
		Double price = 19.99;

		when(bookRepository.findByTitle(title)).thenReturn(Optional.empty());
		assertThrows(ResourceAlreadyExistsException.class, () -> bookService.updateBook(price, title));
		verify(bookRepository, times(1)).findByTitle(title);
		verify(bookRepository, times(0)).save(any(Book.class));
		verify(cacheService, times(0)).addBook(any(Book.class));
	}

	@Test
	void testDeleteBookByTitle_ExistingTitle() {
		String title = "Test Book";

		Book bookCopy = new Book();
		bookCopy.setTitle(book.getTitle());
		bookCopy.setId(book.getId());
		bookCopy.setPublisher(book.getPublisher());
		bookCopy.setAuthors(book.getAuthors());

		when(bookRepository.findByTitle(title)).thenReturn(Optional.of(bookCopy));
		when(publisherRepository.save(bookCopy.getPublisher())).thenReturn(bookCopy.getPublisher());

		bookService.deleteBookByTitle(title);

		verify(bookRepository, times(1)).findByTitle(title);
		verify(cacheService, times(1)).removeBook(bookCopy.getId());
		verify(publisherRepository, times(1)).save(book.getPublisher());
		verify(cacheService, times(1)).updatePublisher(book.getPublisher());
		verify(authorRepository, times(bookCopy.getAuthors().size())).save(any(Author.class));
		verify(cacheService, times(bookCopy.getAuthors().size())).updateAuthor(any(Author.class));
		verify(bookRepository, times(1)).delete(bookCopy);
	}

	@Test
	void testDeleteBookByTitle_NotExistingTitle() {
		String title = "Nonexistent Book";

		when(bookRepository.findByTitle(title)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBookByTitle(title));
		verify(bookRepository, times(1)).findByTitle(title);
		verify(cacheService, times(0)).removeBook(any(Long.class));
		verify(publisherRepository, times(0)).save(any(Publisher.class));
		verify(cacheService, times(0)).updatePublisher(any(Publisher.class));
		verify(authorRepository, times(0)).save(any(Author.class));
		verify(cacheService, times(0)).updateAuthor(any(Author.class));
		verify(bookRepository, times(0)).delete(any(Book.class));
	}


	@Test
	void testFindByAuthorName() {
		List<Book> books = new ArrayList<>();
		books.add(book);

		when(bookRepository.findByAuthorName(anyString())).thenReturn(books);
		when(mapper.apply(book)).thenReturn(bookResponseDTO);

		List<BookResponseDTO> result = bookService.findByAuthorName("Test Author");
		assertEquals(1, result.size());
		assertEquals(bookResponseDTO, result.get(0));
	}

	@Test
	void testUpdateBook() {
		Publisher newPublisher = new Publisher();
		newPublisher.setName("New Publisher");
		newPublisher.setId(2L);

		Author newAuthor = new Author();
		newAuthor.setName("New Author");
		newAuthor.setId(2L);

		List<Author> authors = new ArrayList<>();
		authors.add(newAuthor);


		Book newBook = new Book();
		newBook.setTitle("New Book");
		newBook.setId(2L);
		newBook.setPublisher(newPublisher);
		newBook.setAuthors(authors);

		when(bookRepository.findByTitle(newBook.getTitle())).thenReturn(Optional.of(newBook));
		when(bookRepository.save(newBook)).thenReturn(newBook);

		bookService.updateBook(19.99, newBook.getTitle());

		assertEquals(19.99, newBook.getPrice());

		verify(cacheService, times(1)).addBook(newBook);
	}

}
