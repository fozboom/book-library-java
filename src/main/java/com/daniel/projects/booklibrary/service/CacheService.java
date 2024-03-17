package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.cache.AuthorCache;
import com.daniel.projects.booklibrary.cache.BookCache;
import com.daniel.projects.booklibrary.cache.PublisherCache;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CacheService {
	private final AuthorCache authorCache;
	private final BookCache bookCache;
	private final PublisherCache publisherCache;
	private static final String AUTHOR_PREFIX = "author_";
	private static final String BOOK_PREFIX = "book_";
	private static final String PUBLISHER_PREFIX = "publisher_";

	public void addBook(Book book) {

		bookCache.put(BOOK_PREFIX + book.getId(), book);
	}

	public void addAuthor(Author author) {

		authorCache.put(AUTHOR_PREFIX + author.getId(), author);
	}

	public void addPublisher(Publisher publisher) {

		publisherCache.put(PUBLISHER_PREFIX + publisher.getId(), publisher);
	}

	public Book getBook(Long id) {

		return bookCache.get(BOOK_PREFIX + id);
	}

	public Author getAuthor(Long id) {

		return authorCache.get(AUTHOR_PREFIX + id);
	}

	public Publisher getPublisher(Long id) {

		return publisherCache.get(PUBLISHER_PREFIX + id);
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
			bookCache.remove(BOOK_PREFIX + id);
		}
	}

	public void removeAuthor(Long id) {
		Author author = getAuthor(id);
		if (author != null) {
			for (Book book : author.getBooks()) {
				Book cachedBook = getBook(book.getId());
				if (cachedBook != null) {
					cachedBook.getAuthors().remove(author);
					addBook(cachedBook);
				}
			}
		}
		authorCache.remove(AUTHOR_PREFIX + id);
	}

	public void removePublisher(Long id) {
		Publisher publisher = getPublisher(id);
		if (publisher != null) {
			for (Book book : publisher.getBooks()) {
				Book cachedBook = getBook(book.getId());
				if (cachedBook != null) {
					cachedBook.setPublisher(null);
					addBook(cachedBook);
				}
			}
		}
		publisherCache.remove(PUBLISHER_PREFIX + id);
	}

	public void updateAuthor(Author author) {
		Author cachedAuthor = getAuthor(author.getId());
		if (cachedAuthor != null) {
			for (Book book : cachedAuthor.getBooks()) {
				Book cachedBook = getBook(book.getId());
				if (cachedBook != null) {
					int index = cachedBook.getAuthors().indexOf(cachedAuthor);
					if (index != -1) {
						cachedBook.getAuthors().set(index, author);
						addBook(cachedBook);
					}
				}
			}
		}
		addAuthor(author);
	}

	public void updatePublisher(Publisher publisher) {
		Publisher cachedPublisher = getPublisher(publisher.getId());
		if (cachedPublisher != null) {
			for (Book book : cachedPublisher.getBooks()) {
				Book cachedBook = getBook(book.getId());
				if (cachedBook != null) {
					cachedBook.setPublisher(publisher);
					addBook(cachedBook);
				}
			}
		}
		addPublisher(publisher);
	}

	public void updateBook(Book book) {
		Book cachedBook = getBook(book.getId());
		if (cachedBook != null) {
			Publisher cachedPublisher = getPublisher(cachedBook.getPublisher().getId());
			if (cachedPublisher != null) {
				int index = cachedPublisher.getBooks().indexOf(cachedBook);
				if (index != -1) {
					cachedPublisher.getBooks().set(index, book);
					addPublisher(cachedPublisher);
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
