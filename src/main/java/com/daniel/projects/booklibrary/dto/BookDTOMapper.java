package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class BookDTOMapper implements Function<Book, BookDTO> {

	private final AuthorDTOMapper authorMapper = new AuthorDTOMapper();
	private final PublisherWithoutBooksDTOMapper publisherMapper = new PublisherWithoutBooksDTOMapper();

	@Override
	public BookDTO apply(Book book) {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors().stream().map(authorMapper).toList());
		bookDTO.setPublisher(publisherMapper.apply(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());
		return bookDTO;
	}

	public BookDTO toDTO(Book book) {
		if (book == null) {
			return null;
		}

		BookDTO bookDTO = new BookDTO();

		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors().stream().map(authorMapper).toList());

		bookDTO.setPublisher(publisherMapper.toDTO(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());

		return bookDTO;
	}
}
