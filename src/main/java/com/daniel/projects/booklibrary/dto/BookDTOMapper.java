package com.daniel.projects.booklibrary.dto;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookDTOMapper implements Function<Book, BookDTO> {
	@Override
    public BookDTO apply(Book book) {

		return new BookDTO(book.getTitle());
    }
}
