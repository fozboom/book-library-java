package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.cache.AuthorCache;
import com.daniel.projects.booklibrary.cache.BookCache;
import com.daniel.projects.booklibrary.cache.PublisherCache;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Fields;
import com.daniel.projects.booklibrary.model.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CacheService {
	private final AuthorCache authorCache;
	private final BookCache bookCache;
	private final PublisherCache publisherCache;


	public void addBook(Book book) {

		bookCache.put(Fields.bookPrefix + book.getId(), book);
	}

	public void addAuthor(Author author) {

		authorCache.put(Fields.authorPrefix + author.getId(), author);
	}

	public void addPublisher(Publisher publisher) {

		publisherCache.put(Fields.publisherPrefix + publisher.getId(), publisher);
	}

	public Book getBook(Long id) {

		return bookCache.get(Fields.bookPrefix + id);
	}

	public Author getAuthor(Long id) {

		return authorCache.get(Fields.authorPrefix + id);
	}

	public Publisher getPublisher(Long id) {

		return publisherCache.get(Fields.publisherPrefix + id);
	}

	public void removeBook(Long id) {
		Book book = getBook(id);
		if (book != null) {
			for (Author author : book.getAuthors()) {
				Author cachedAuthor = getAuthor(author.getId());
				if (cachedAuthor != null) {
					cachedAuthor.getBooks().remove(book);
					addAuthor(cachedAuthor);
				}
			}
			Publisher publisher = book.getPublisher();
			if (publisher != null) {
				Publisher cachedPublisher = getPublisher(publisher.getId());
				if (cachedPublisher != null) {
					cachedPublisher.getBooks().remove(book);
					addPublisher(cachedPublisher);
				}
			}
			bookCache.remove(Fields.bookPrefix + id);
		}
	}

	public void removeAuthor(Long id) {
		for (Book book : bookCache.values()) {
			if (book.getAuthors().removeIf(authorInBook -> authorInBook.getId().equals(id))) {
				updateBook(book);
			}
		}

		authorCache.remove(Fields.authorPrefix + id);
	}

	public void removePublisher(Long id) {
		for (Book book : bookCache.values()) {
			if (book.getPublisher() != null && book.getPublisher().getId().equals(id)) {
				book.setPublisher(null);
				updateBook(book);
			}
		}

		publisherCache.remove(Fields.publisherPrefix + id);
	}

	public void updateAuthor(Author author) {
		for (Book book : bookCache.values()) {
			for (Author bookAuthor : book.getAuthors()) {
				if (bookAuthor.getId().equals(author.getId())) {
					int index = book.getAuthors().indexOf(bookAuthor);
					book.getAuthors().set(index, author);
					updateBook(book);
					break;
				}
			}
		}

		addAuthor(author);
	}

	public void updatePublisher(Publisher publisher) {
		for (Book book : bookCache.values()) {
			if (book.getPublisher() != null && book.getPublisher().getId().equals(publisher.getId())) {
				book.setPublisher(publisher);
				updateBook(book);
			}
		}

		addPublisher(publisher);
	}

	public void updateBook(Book book) {
		Book cachedBook = getBook(book.getId());
		if (cachedBook != null) {
			Publisher publisher = cachedBook.getPublisher();
			if (publisher != null) {
				Publisher cachedPublisher = getPublisher(publisher.getId());
				if (cachedPublisher != null) {
					int index = cachedPublisher.getBooks().indexOf(cachedBook);
					if (index != -1) {
						cachedPublisher.getBooks().set(index, book);
						addPublisher(cachedPublisher);
					}
				}
			}

			for (Author author : cachedBook.getAuthors()) {
				Author cachedAuthor = getAuthor(author.getId());
				if (cachedAuthor != null) {
					int index = cachedAuthor.getBooks().indexOf(cachedBook);
					if (index != -1) {
						cachedAuthor.getBooks().set(index, book);
						addAuthor(cachedAuthor);
					}
				}
			}
		}
		addBook(book);
	}
}

