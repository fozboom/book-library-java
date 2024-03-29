package com.daniel.projects.booklibrary.service;


import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import com.daniel.projects.booklibrary.repository.BookRepository;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@AllArgsConstructor
@Transactional
public class BookService {
	private final CacheService cacheService;
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final BookResponseDTOMapper mapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

	public List<BookResponseDTO> findAllBooks() {

		return bookRepository.findAllBooks().stream().map(mapper).toList();
	}


	public Optional<Book> addBook(final Book book) {
		if (bookRepository.existsByTitle(book.getTitle())) {
			return Optional.empty();
		}

		handlePublisher(book);
		handleAuthors(book);

		Book savedBook = bookRepository.save(book);
		cacheService.addBook(savedBook);

		updatePublisherInCache(savedBook);
		updateAuthorsInCache(savedBook);

		return Optional.of(savedBook);
	}


	public BookResponseDTO findByTitle(final String title) {
		Book book = bookRepository.findByTitle(title);

		if (book == null) {
			return null;
		}
		if (cacheService.getBook(book.getId()) == null) {
			cacheService.addBook(book);
			LOGGER.info("Book retrieved from " + "repository and added to cache");
		}
		return mapper.apply(book);
	}


	public BookResponseDTO findById(final Long id) {
		Book book = cacheService.getBook(id);

		if (book == null) {
			Optional<Book> optionalBook = bookRepository.findBookById(id);

			if (optionalBook.isEmpty()) {
				return null;
			}

			Book retrievedBook = optionalBook.get();

			cacheService.addBook(retrievedBook);
			LOGGER.info("Book retrieved from " + "repository and added to cache");
			return mapper.apply(retrievedBook);
		} else {
			LOGGER.info("Book retrieved from cache");
		}

		return mapper.apply(book);
	}

	public List<BookResponseDTO> findByAuthorName(final String author) {
		List<Book> books = bookRepository.findByAuthorName(author);
		return books.stream().map(mapper).toList();
	}


	public boolean updateBook(final Double price, final String title) {
		Book book = bookRepository.findByTitle(title);
		if (book == null) {
			return false;
		}
		book.setPrice(price);
		bookRepository.save(book);
		cacheService.addBook(book);
		return true;
	}

	public boolean deleteBookByTitle(final String title) {
		Book book = bookRepository.findByTitle(title);

		if (book != null) {
			cacheService.removeBook(book.getId());

			Publisher publisher = book.getPublisher();
			List<Author> authors = new ArrayList<>(book.getAuthors());

			if (publisher != null) {
				publisher.removeBook(book);
				publisherRepository.save(publisher);
				cacheService.updatePublisher(publisher);
			}

			for (Author author : authors) {
				author.removeBook(book);
				authorRepository.save(author);
				cacheService.updateAuthor(author);
			}

			book.setAuthors(null);
			book.setPublisher(null);
			bookRepository.delete(book);

			return true;
		}

		return false;
	}


	private void handlePublisher(final Book book) {
		Publisher publisher = book.getPublisher();
		Publisher existingPublisher = publisherRepository.findByName(publisher.getName());

		if (existingPublisher != null) {
			book.setPublisher(existingPublisher);
		} else {
			Publisher savedPublisher = publisherRepository.save(publisher);
			book.setPublisher(savedPublisher);
		}
	}

	private void handleAuthors(final Book book) {
		List<Author> authors = book.getAuthors();
		List<Author> finalAuthors = new ArrayList<>();

		for (Author author : authors) {
			Optional<Author> existingAuthor = authorRepository.findAuthorByName(author.getName());
			if (existingAuthor.isPresent()) {
				finalAuthors.add(existingAuthor.get());
			} else {
				Author savedAuthor = authorRepository.save(author);
				finalAuthors.add(savedAuthor);
			}
		}

		book.setAuthors(finalAuthors);
	}

	private void updatePublisherInCache(final Book savedBook) {
		Publisher publisherInCache = cacheService.getPublisher(savedBook.getPublisher().getId());
		if (publisherInCache != null) {
			publisherInCache.addBook(savedBook);
			cacheService.updatePublisher(publisherInCache);
		}
	}

	private void updateAuthorsInCache(final Book savedBook) {
		for (Author author : savedBook.getAuthors()) {
			Author authorInCache = cacheService.getAuthor(author.getId());
			if (authorInCache != null) {
				authorInCache.addBook(savedBook);
				cacheService.updateAuthor(authorInCache);
			}
		}
	}
}
