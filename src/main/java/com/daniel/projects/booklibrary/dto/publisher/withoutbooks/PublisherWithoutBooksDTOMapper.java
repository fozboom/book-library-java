package com.daniel.projects.booklibrary.dto.publisher.withoutbooks;

import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PublisherWithoutBooksDTOMapper
		implements Function<Publisher, PublisherWithoutBooksDTO> {
	@Override
	public PublisherWithoutBooksDTO apply(final Publisher publisher) {
		if (publisher == null) {
			return null;
		}

		PublisherWithoutBooksDTO publisherDTO =
				new PublisherWithoutBooksDTO();

		publisherDTO.setName(publisher.getName());
		publisherDTO.setAddress(publisher.getAddress());
		publisherDTO.setId(publisher.getId());
		return publisherDTO;
	}
}
