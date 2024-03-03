package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PublisherDTOMapper implements Function<Publisher, PublisherDTO> {
	private final BookDTOMapper bookMapper;
	public PublisherDTOMapper(BookDTOMapper bookMapper) {

		this.bookMapper = bookMapper;
	}

	@Override
	public PublisherDTO apply(Publisher publisher) {
		List<BookDTO> bookDTOs = publisher.getBooks().stream()
				.map(bookMapper)
				.collect(Collectors.toList());

		return new PublisherDTO(
				publisher.getName(),
				bookDTOs,
				publisher.getAddress());
	}

}
