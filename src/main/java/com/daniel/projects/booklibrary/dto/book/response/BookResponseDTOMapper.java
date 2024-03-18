package com.daniel.projects.booklibrary.dto.book.response;


import com.daniel.projects.booklibrary.dto.publisher.withoutbooks.PublisherWithoutBooksDTOMapper;
import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTOMapper;
import com.daniel.projects.booklibrary.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
@AllArgsConstructor
public class BookResponseDTOMapper implements Function<Book, BookResponseDTO> {

	private final AuthorNameDTOMapper authorMapper = new AuthorNameDTOMapper();
	private final PublisherWithoutBooksDTOMapper publisherMapper = new PublisherWithoutBooksDTOMapper();

	@Override
	public BookResponseDTO apply(Book book) {
		if (book == null) {
			return null;
		}

		BookResponseDTO bookDTO = new BookResponseDTO();


		bookDTO.setTitle(book.getTitle());
		bookDTO.setAuthors(book.getAuthors().stream().map(authorMapper).toList());
		bookDTO.setId(book.getId());
		bookDTO.setPublisher(publisherMapper.apply(book.getPublisher()));
		bookDTO.setPrice(book.getPrice());

		return bookDTO;
	}

}
