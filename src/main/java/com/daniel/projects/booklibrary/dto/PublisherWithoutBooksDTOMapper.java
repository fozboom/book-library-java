package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PublisherWithoutBooksDTOMapper implements Function<Publisher, PublisherWithoutBooksDTO> {
	@Override
	public PublisherWithoutBooksDTO apply(Publisher publisher) {
		PublisherWithoutBooksDTO publisherDTO = new PublisherWithoutBooksDTO();
		publisherDTO.setName(publisher.getName());
		publisherDTO.setAddress(publisher.getAddress());
		return publisherDTO;
	}

	public PublisherWithoutBooksDTO toDTO(Publisher publisher) {
		if (publisher == null) {
            return null;
        }

        PublisherWithoutBooksDTO publisherDTO = new PublisherWithoutBooksDTO();

        publisherDTO.setName(publisher.getName());
        publisherDTO.setAddress(publisher.getAddress());

        return publisherDTO;
	}
}
