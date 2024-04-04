package com.daniel.projects.booklibrary.mapper;

import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Publisher;

import java.util.function.Function;

public class PublisherSaveDTOMapper
		implements Function<Publisher, PublisherSaveDTO> {
	@Override
    public PublisherSaveDTO apply(final Publisher publisher) {
        return new PublisherSaveDTO(publisher.getName(),
				publisher.getAddress());
    }
}
