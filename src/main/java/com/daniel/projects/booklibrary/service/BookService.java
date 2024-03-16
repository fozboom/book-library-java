package com.daniel.projects.booklibrary.service;


import com.daniel.projects.booklibrary.cache.InMemoryCache;
import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import com.daniel.projects.booklibrary.repository.BookRepository;
import com.daniel.projects.booklibrary.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.*;


@Service
@AllArgsConstructor
@Transactional
public class BookService {
	private final InMemoryCache<Long, Book> bookCache = new InMemoryCache<>(100);
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final BookResponseDTOMapper mapper;
	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	public List<BookResponseDTO> findAllBooks() {

		return bookRepository.findAll().stream().map(mapper).toList();
	}


	public Optional<Book> addBook(Book book) {
		if (bookRepository.existsByTitle(book.getTitle())) {
			return Optional.empty();
		}

		Publisher publisher = book.getPublisher();
		Publisher existingPublisher = publisherRepository.findByName(publisher.getName());

		if (existingPublisher != null) {
			book.setPublisher(existingPublisher);
		} else {
			Publisher savedPublisher = publisherRepository.save(publisher);
			book.setPublisher(savedPublisher);
		}

		List<Author> authors = book.getAuthors();
		List<Author> finalAuthors = new ArrayList<>();

		for (Author author : authors) {
			Author existingAuthor = authorRepository.findByName(author.getName());
			if (existingAuthor != null) {
				finalAuthors.add(existingAuthor);
			} else {
				Author savedAuthor = authorRepository.save(author);
				finalAuthors.add(savedAuthor);
			}
		}

		book.setAuthors(finalAuthors);
		bookCache.put(book.getId(), book);
		return Optional.of(bookRepository.save(book));
	}


	public BookResponseDTO findByTitle(String title) {
		Book book = bookRepository.findByTitle(title);

		if (book == null) {
			return null;
		}
		if (bookCache.get(book.getId()) == null) {
			bookCache.put(book.getId(), book);
			logger.info("Book with title {} retrieved from repository and added to cache", title);
		}
		return mapper.apply(book);
	}


	public BookResponseDTO findById(Long id) {
		Book book = bookCache.get(id);

		if (book == null) {
			Optional<Book> optionalBook = bookRepository.findById(id);

			if (optionalBook.isEmpty()) {
				return null;
			}

			Book retrievedBook = optionalBook.get();

			bookCache.put(id, retrievedBook);
			logger.info("Book with id {} retrieved from repository and added to cache", id);
			return mapper.apply(retrievedBook);
		} else {
			logger.info("Book with id {} retrieved from cache", id);
		}

		return mapper.apply(book);
	}

	public List<BookResponseDTO> findByAuthorName(String author) {
		List<Book> books = bookRepository.findByAuthorName(author);
		return books.stream().map(mapper).toList();
	}


	public boolean updateBook(Double price, String title) {
		Book book = bookRepository.findByTitle(title);
		if (book == null) {
			return false;
		}
		book.setPrice(price);
		bookRepository.save(book);
		bookCache.put(book.getId(), book);
		return true;
	}

	public boolean deleteBookByTitle(String title) {

		Book book = bookRepository.findByTitle(title);

		if (book != null) {
			bookRepository.delete(book);
			bookCache.remove(book.getId());
			return true;
		}
		return false;
	}
}
