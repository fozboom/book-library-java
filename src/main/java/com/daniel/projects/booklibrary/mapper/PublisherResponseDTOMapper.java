package com.daniel.projects.booklibrary.mapper;

import com.daniel.projects.booklibrary.dto.book.title.BookTitleDTO;
import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.mapper.BookTitleDTOMapper;
import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PublisherResponseDTOMapper
		implements Function<Publisher, PublisherResponseDTO> {
	private final BookTitleDTOMapper bookMapper;
	public PublisherResponseDTOMapper(final BookTitleDTOMapper mapper) {

		this.bookMapper = mapper;
	}

	@Override
	public PublisherResponseDTO apply(final Publisher publisher) {
		List<BookTitleDTO> bookDTOs = publisher.getBooks().stream()
				.map(bookMapper)
				.collect(Collectors.toList());

		return new PublisherResponseDTO(
				publisher.getId(),
				publisher.getName(),
				bookDTOs,
				publisher.getAddress());
	}

}
