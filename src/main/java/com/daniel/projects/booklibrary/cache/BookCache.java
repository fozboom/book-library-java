package com.daniel.projects.booklibrary.cache;


import com.daniel.projects.booklibrary.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class BookCache extends InMemoryCache<String, Book> {
	public BookCache(@Value("${cache.maxSize}") final Integer maxSize) {

		super(maxSize);
	}
}


