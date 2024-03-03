package com.daniel.projects.booklibrary.service;


import com.daniel.projects.booklibrary.dto.BookDTO;
import com.daniel.projects.booklibrary.dto.BookDTOMapper;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BookService {
	private final BookRepository repository;
	private final BookDTOMapper mapper;
	public List<BookDTO> findAllBooks() {

		return repository.findAll().stream().map(mapper).toList();
	}


	public Optional<Book> addBook(Book book) {
		if (repository.existsByTitle(book.getTitle())) {
			return Optional.empty();
		}

		return Optional.of(repository.save(book));
	}


	public BookDTO findByTitle(String title) {
		Book book = repository.findByTitle(title);

		if (book == null) {
			return null;
		}

		return mapper.toDTO(book);
	}

	public boolean updateBook(Double price, String title) {
		Book book = repository.findByTitle(title);
		if (book == null) {
			return false;
		}
		book.setTitle(title);
		book.setPrice(price);
		repository.save(book);
		return true;
	}

	@Transactional
	public boolean deleteBookByTitle(String title) {

		Book book = repository.findByTitle(title);

		if (book != null) {
			repository.delete(book);
			return true;
		}
		return false;
	}
}
