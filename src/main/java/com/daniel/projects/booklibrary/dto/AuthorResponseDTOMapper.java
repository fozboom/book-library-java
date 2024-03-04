package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class AuthorResponseDTOMapper implements Function<Author, AuthorResponseDTO> {
	@Override
	public AuthorResponseDTO apply(Author author) {
		List<BookTitleDTO> bookTitleDTOs = author.getBooks().stream()
				.map(book -> new BookTitleDTO(book.getTitle())).toList();

		return new AuthorResponseDTO(author.getName(), bookTitleDTOs);
	}

	public AuthorResponseDTO toDTO(Author author) {
		List<BookTitleDTO> bookTitleDTOs = author.getBooks().stream()
				.map(book -> new BookTitleDTO(book.getTitle()))
				.toList();

		return new AuthorResponseDTO(author.getName(), bookTitleDTOs);
	}

}
