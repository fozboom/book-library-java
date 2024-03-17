package com.daniel.projects.booklibrary.cache;


import com.daniel.projects.booklibrary.model.Author;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorCache extends InMemoryCache<String, Author> {
	public AuthorCache(@Value("${cache.maxSize}") Integer maxSize) {
		super(maxSize);
	}
}


