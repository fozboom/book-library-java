package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AuthorDTOMapper implements Function<Author, AuthorDTO> {
	@Override
    public AuthorDTO apply(Author author) {

        return new AuthorDTO(author.getName());
    }
}
