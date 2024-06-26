package com.daniel.projects.booklibrary.mapper;

import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.dto.book.title.BookTitleDTO;
import com.daniel.projects.booklibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class AuthorResponseDTOMapper
		implements Function<Author, AuthorResponseDTO> {
	@Override
	public AuthorResponseDTO apply(final Author author) {
		List<BookTitleDTO> bookTitleDTOs = author.getBooks().stream()
				.map(book -> new BookTitleDTO(book
						.getTitle())).toList();

		return new AuthorResponseDTO(author.getId(),
				author.getName(), bookTitleDTOs);
	}


}
