package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class BookResponseDTOMapper implements Function<Book, BookResponseDTO> {

	private final AuthorNameDTOMapper authorMapper = new AuthorNameDTOMapper();
	private final PublisherWithoutBooksDTOMapper publisherMapper = new PublisherWithoutBooksDTOMapper();

	@Override
	public BookResponseDTO apply(Book book) {
		BookResponseDTO bookDTO = new BookResponseDTO();
		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors().stream().map(authorMapper).toList());
		bookDTO.setPublisher(publisherMapper.apply(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());
		return bookDTO;
	}

	public BookResponseDTO toDTO(Book book) {
		if (book == null) {
			return null;
		}

		BookResponseDTO bookDTO = new BookResponseDTO();

		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors().stream().map(authorMapper).toList());

		bookDTO.setPublisher(publisherMapper.toDTO(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());

		return bookDTO;
	}
}
