package com.daniel.projects.booklibrary.dto.book.title;

import com.daniel.projects.booklibrary.model.Book;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BookTitleDTOMapper implements Function<Book, BookTitleDTO> {
	@Override
    public BookTitleDTO apply(Book book) {

        return new BookTitleDTO(book.getTitle());
    }
}
