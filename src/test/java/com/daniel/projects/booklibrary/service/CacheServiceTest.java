package com.daniel.projects.booklibrary.service;


import com.daniel.projects.booklibrary.cache.AuthorCache;
import com.daniel.projects.booklibrary.cache.BookCache;
import com.daniel.projects.booklibrary.cache.PublisherCache;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.model.Fields;
import com.daniel.projects.booklibrary.model.Publisher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

	@Mock
	private AuthorCache authorCache;

	@Mock
	private BookCache bookCache;

	@Mock
	private PublisherCache publisherCache;

	@InjectMocks
	private CacheService cacheService;

	private Book book;
	private Author author;
	private Publisher publisher;

	@BeforeEach
	void setUp() {
		book = new Book();
		book.setId(1L);

		author = new Author();
		author.setId(1L);

		publisher = new Publisher();
		publisher.setId(1L);
	}

	@Test
	void testAddBook() {
		cacheService.addBook(book);
		verify(bookCache, times(1)).put("book_1", book);
	}

	@Test
	void testAddAuthor() {
		cacheService.addAuthor(author);
		verify(authorCache, times(1)).put("author_1", author);
	}

	@Test
	void testAddPublisher() {
		cacheService.addPublisher(publisher);
		verify(publisherCache, times(1)).put("publisher_1", publisher);
	}

	@Test
	void testGetBook() {
		when(bookCache.get("book_1")).thenReturn(book);
		Book result = cacheService.getBook(1L);
		assertEquals(book, result);
	}

	@Test
	void testGetAuthor() {
		when(authorCache.get("author_1")).thenReturn(author);
		Author result = cacheService.getAuthor(1L);
		assertEquals(author, result);
	}

	@Test
	void testGetPublisher() {
		when(publisherCache.get("publisher_1")).thenReturn(publisher);
		Publisher result = cacheService.getPublisher(1L);
		assertEquals(publisher, result);
	}

	@Test
	void testGetBookWhenNotInCache() {
		when(bookCache.get("book_1")).thenReturn(null);
		Book result = cacheService.getBook(1L);
		assertNull(result);
	}

	@Test
	void testGetAuthorWhenNotInCache() {
		when(authorCache.get("author_1")).thenReturn(null);
		Author result = cacheService.getAuthor(1L);
		assertNull(result);
	}

	@Test
	void testGetPublisherWhenNotInCache() {
		when(publisherCache.get("publisher_1")).thenReturn(null);
		Publisher result = cacheService.getPublisher(1L);
		assertNull(result);
	}

	@Test
	void testRemoveBookWhenBookExistsInCache() {
		book.getAuthors().add(author);
		book.setPublisher(publisher);
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(book);
		when(authorCache.get(Fields.AUTHOR_PREFIX + author.getId())).thenReturn(author);
		when(publisherCache.get(Fields.PUBLISHER_PREFIX + publisher.getId())).thenReturn(publisher);

		cacheService.removeBook(1L);

		verify(bookCache).remove(Fields.BOOK_PREFIX + book.getId());
		verify(authorCache).put(Fields.AUTHOR_PREFIX + author.getId(), author);
		verify(publisherCache).put(Fields.PUBLISHER_PREFIX + publisher.getId(), publisher);
	}

	@Test
	void testRemoveBookWhenBookNotInCache() {
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(null);

		cacheService.removeBook(1L);

		verify(bookCache, never()).remove(anyString());
		verify(authorCache, never()).put(anyString(), any(Author.class));
		verify(publisherCache, never()).put(anyString(), any(Publisher.class));
	}

	@Test
	void testRemoveBookWhenAuthorNotInCache() {
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(book);

		cacheService.removeBook(1L);

		verify(bookCache).remove(Fields.BOOK_PREFIX + book.getId());
		verify(authorCache, never()).put(anyString(), any(Author.class));
		verify(publisherCache, never()).put(anyString(), any(Publisher.class));
	}

	@Test
	void testRemoveBookWhenPublisherNotInCache() {
		book.getAuthors().add(author);
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(book);


		cacheService.removeBook(1L);

		verify(bookCache).remove(Fields.BOOK_PREFIX + book.getId());
		verify(authorCache, never()).put(anyString(), any(Author.class));
		verify(publisherCache, never()).put(anyString(), any(Publisher.class));
	}

	@Test
	void testRemoveAuthorWhenAuthorExistsInCache() {
		when(authorCache.get(Fields.AUTHOR_PREFIX + author.getId())).thenReturn(author);

		book.getAuthors().add(author);

		cacheService.removeAuthor(1L);

		verify(authorCache).remove(Fields.AUTHOR_PREFIX + author.getId());
	}

	@Test
	void testRemoveAuthorWhenAuthorNotInCache() {
		when(authorCache.get(Fields.AUTHOR_PREFIX + author.getId())).thenReturn(null);

		cacheService.removeAuthor(1L);

		verify(authorCache, never()).remove(anyString());
	}

	@Test
	void testRemovePublisherWhenPublisherExistsInCache() {
		when(publisherCache.get(Fields.PUBLISHER_PREFIX + publisher.getId())).thenReturn(publisher);

		book.setPublisher(publisher);

		cacheService.removePublisher(1L);

		verify(publisherCache).remove(Fields.PUBLISHER_PREFIX + publisher.getId());
	}

	@Test
	void testRemovePublisherWhenPublisherNotInCache() {
		when(publisherCache.get(Fields.PUBLISHER_PREFIX + publisher.getId())).thenReturn(null);

		cacheService.removePublisher(1L);

		verify(publisherCache, never()).remove(anyString());
	}

	@Test
	void testUpdateAuthorWhenAuthorExistsInCache() {
		book.getAuthors().add(author);
		when(bookCache.values()).thenReturn(Collections.singletonList(book));

		cacheService.updateAuthor(author);

		verify(authorCache).put(Fields.AUTHOR_PREFIX + author.getId(), author);
		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
	}

	@Test
	void testUpdateAuthorWhenAuthorNotInCache() {
		when(bookCache.values()).thenReturn(Collections.emptyList());

		cacheService.updateAuthor(author);

		verify(authorCache).put(Fields.AUTHOR_PREFIX + author.getId(), author);
		verify(bookCache, never()).put(anyString(), any(Book.class));
	}

	@Test
	void testUpdateAuthorWhenAuthorExistsInBook() {
		book.getAuthors().add(author);
		when(bookCache.values()).thenReturn(Collections.singletonList(book));

		Author updatedAuthor = new Author();
		updatedAuthor.setId(author.getId());
		updatedAuthor.setName("Updated Name");

		cacheService.updateAuthor(updatedAuthor);

		verify(authorCache).put(Fields.AUTHOR_PREFIX + updatedAuthor.getId(), updatedAuthor);
		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
		assertEquals(updatedAuthor, book.getAuthors().get(0));
	}

	@Test
	void testUpdatePublisherWhenPublisherExistsInCache() {
		book.setPublisher(publisher);
		when(bookCache.values()).thenReturn(Collections.singletonList(book));

		cacheService.updatePublisher(publisher);

		verify(publisherCache).put(Fields.PUBLISHER_PREFIX + publisher.getId(), publisher);
		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
	}

	@Test
	void testUpdatePublisherWhenPublisherNotInCache() {
		when(bookCache.values()).thenReturn(Collections.emptyList());

		cacheService.updatePublisher(publisher);

		verify(publisherCache).put(Fields.PUBLISHER_PREFIX + publisher.getId(), publisher);
		verify(bookCache, never()).put(anyString(), any(Book.class));
	}

	@Test
	void testUpdatePublisherWhenPublisherExistsInBook() {
		book.setPublisher(publisher);
		when(bookCache.values()).thenReturn(Collections.singletonList(book));

		Publisher updatedPublisher = new Publisher();
		updatedPublisher.setId(publisher.getId());
		updatedPublisher.setName("Updated Name");

		cacheService.updatePublisher(updatedPublisher);

		verify(publisherCache).put(Fields.PUBLISHER_PREFIX + updatedPublisher.getId(), updatedPublisher);
		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
		assertEquals(updatedPublisher, book.getPublisher());
	}

	@Test
	void testUpdateBookWhenBookExistsInCache() {
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(book);

		cacheService.updateBook(book);

		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
	}

	@Test
	void testUpdateBookWhenBookNotInCache() {
		when(bookCache.get(Fields.BOOK_PREFIX + book.getId())).thenReturn(null);

		cacheService.updateBook(book);

		verify(bookCache).put(Fields.BOOK_PREFIX + book.getId(), book);
	}

	@Test
	void testUpdateBook() {
		Book cachedBook = mock(Book.class);
		Book newBook = mock(Book.class);
		Publisher publisher = mock(Publisher.class);
		Author author = mock(Author.class);

		when(cachedBook.getPublisher()).thenReturn(publisher);
		when(cachedBook.getAuthors()).thenReturn(Collections.singletonList(author));
		when(bookCache.get(Fields.BOOK_PREFIX + cachedBook.getId())).thenReturn(cachedBook);
		when(publisherCache.get(Fields.PUBLISHER_PREFIX + publisher.getId())).thenReturn(publisher);
		when(authorCache.get(Fields.AUTHOR_PREFIX + author.getId())).thenReturn(author);


		cacheService.updateBook(newBook);


		verify(publisher).getBooks();
		verify(author).getBooks();
	}





}
