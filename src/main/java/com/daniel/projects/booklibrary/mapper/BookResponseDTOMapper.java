package com.daniel.projects.booklibrary.mapper;


import com.daniel.projects.booklibrary.dto.book.response.BookResponseDTO;
import com.daniel.projects.booklibrary.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
@AllArgsConstructor
public class BookResponseDTOMapper
		implements Function<Book, BookResponseDTO> {

	private final AuthorNameDTOMapper authorMapper =
			new AuthorNameDTOMapper();
	private final PublisherWithoutBooksDTOMapper publisherMapper =
			new PublisherWithoutBooksDTOMapper();

	@Override
	public BookResponseDTO apply(final Book book) {
		if (book == null) {
			return null;
		}

		BookResponseDTO bookDTO = new BookResponseDTO();


		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors()
				.stream().map(authorMapper).toList());
		bookDTO.setId(book.getId());
		bookDTO.setPublisher(publisherMapper
				.apply(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());

		return bookDTO;
	}

}
