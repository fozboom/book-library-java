package com.daniel.projects.booklibrary.cache;


import com.daniel.projects.booklibrary.model.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PublisherCache extends InMemoryCache<String, Publisher> {
	public PublisherCache(@Value("${cache.maxSize}") final Integer maxSize) {
		super(maxSize);
	}
}
