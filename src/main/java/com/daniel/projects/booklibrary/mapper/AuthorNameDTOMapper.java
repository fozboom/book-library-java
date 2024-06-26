package com.daniel.projects.booklibrary.mapper;

import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTO;
import com.daniel.projects.booklibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AuthorNameDTOMapper implements Function<Author, AuthorNameDTO> {
	@Override
    public AuthorNameDTO apply(final Author author) {

        return new AuthorNameDTO(author.getId(), author.getName());
    }
}
