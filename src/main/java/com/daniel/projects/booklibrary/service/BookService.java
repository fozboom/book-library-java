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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
	private final BookRepository repository;
	private final BookDTOMapper mapper;
	public List<BookDTO> findAllBooks() {
		return repository.findAll().
				stream().map(mapper).collect(Collectors.toList());
	}


	public Book addBook(Book book) {

		return repository.save(book);
	}


	public BookDTO findByTitle(String title) {
		Optional<BookDTO> result = repository.findByTitle(title).map(mapper);
		if(result.isEmpty()) {
			throw new RuntimeException("User not found");
		}
		return result.get();
	}


	@Transactional
	public String deleteBook(String bookName) {
		int res = repository.deleteByTitle(bookName);
		if (res > 0) {
			return "Good";
		}
		return "No element found";
	}
}
