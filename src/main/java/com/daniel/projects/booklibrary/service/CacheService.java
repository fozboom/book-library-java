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

		bookCache.put(Fields.BOOK_PREFIX + book.getId(), book);
	}

	public void addAuthor(Author author) {

		authorCache.put(Fields.AUTHOR_PREFIX + author.getId(), author);
	}

	public void addPublisher(Publisher publisher) {

		publisherCache.put(Fields.PUBLISHER_PREFIX + publisher.getId(), publisher);
	}

	public Book getBook(Long id) {

		return bookCache.get(Fields.BOOK_PREFIX + id);
	}

	public Author getAuthor(Long id) {

		return authorCache.get(Fields.AUTHOR_PREFIX + id);
	}

	public Publisher getPublisher(Long id) {

		return publisherCache.get(Fields.PUBLISHER_PREFIX + id);
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
			bookCache.remove(Fields.BOOK_PREFIX + id);
		}
	}

	public void removeAuthor(Long id) {
		for (Book book : bookCache.values()) {
			if (book.getAuthors().removeIf(authorInBook -> authorInBook.getId().equals(id))) {
				updateBook(book);
			}
		}

		authorCache.remove(Fields.AUTHOR_PREFIX + id);
	}

	public void removePublisher(Long id) {
		for (Book book : bookCache.values()) {
			if (book.getPublisher() != null && book.getPublisher().getId().equals(id)) {
				book.setPublisher(null);
				updateBook(book);
			}
		}

		publisherCache.remove(Fields.PUBLISHER_PREFIX + id);
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
			updatePublisher(cachedBook, book);
			updateAuthors(cachedBook, book);
		}
		addBook(book);
	}

	private void updatePublisher(Book cachedBook, Book book) {
		Publisher publisher = cachedBook.getPublisher();
		if (publisher != null) {
			Publisher cachedPublisher = getPublisher(publisher.getId());
			if (cachedPublisher != null) {
				replaceBookInPublisher(cachedPublisher, cachedBook, book);
			}
		}
	}

	private void replaceBookInPublisher(Publisher publisher, Book oldBook, Book newBook) {
		int index = publisher.getBooks().indexOf(oldBook);
		if (index != -1) {
			publisher.getBooks().set(index, newBook);
			addPublisher(publisher);
		}
	}

	private void updateAuthors(Book cachedBook, Book book) {
		for (Author author : cachedBook.getAuthors()) {
			Author cachedAuthor = getAuthor(author.getId());
			if (cachedAuthor != null) {
				replaceBookInAuthor(cachedAuthor, cachedBook, book);
			}
		}
	}

	private void replaceBookInAuthor(Author author, Book oldBook, Book newBook) {
		int index = author.getBooks().indexOf(oldBook);
		if (index != -1) {
			author.getBooks().set(index, newBook);
			addAuthor(author);
		}
	}
}

