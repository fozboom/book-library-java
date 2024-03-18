package com.daniel.projects.booklibrary.dto.author.name;

import com.daniel.projects.booklibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AuthorNameDTOMapper implements Function<Author, AuthorNameDTO> {
	@Override
    public AuthorNameDTO apply(Author author) {

        return new AuthorNameDTO(author.getId(), author.getName());
    }
}
