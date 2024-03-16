package com.daniel.projects.booklibrary.service;


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
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
@Transactional
public class BookService {
	private final BookRepository repository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final BookResponseDTOMapper mapper;

	public List<BookResponseDTO> findAllBooks() {

		return repository.findAll().stream().map(mapper).toList();
	}


	public Optional<Book> addBook(Book book) {
		if (repository.existsByTitle(book.getTitle())) {
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
		return Optional.of(repository.save(book));
	}


	public BookResponseDTO findByTitle(String title) {
		Book book = repository.findByTitle(title);

		if (book == null) {
			return null;
		}

		return mapper.apply(book);
	}

	public List<BookResponseDTO> findByAuthorName(String author) {
		List<Book> books = repository.findByAuthorName(author);
		return books.stream().map(mapper).toList();
	}


	public boolean updateBook(Double price, String title) {
		Book book = repository.findByTitle(title);
		if (book == null) {
			return false;
		}
		book.setPrice(price);
		repository.save(book);
		return true;
	}

	public boolean deleteBookByTitle(String title) {

		Book book = repository.findByTitle(title);

		if (book != null) {
			repository.delete(book);
			return true;
		}
		return false;
	}
}
