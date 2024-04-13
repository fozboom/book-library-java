package com.daniel.projects.booklibrary.service;
import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.mapper.AuthorResponseDTOMapper;
import com.daniel.projects.booklibrary.exception.ResourceAlreadyExistsException;
import com.daniel.projects.booklibrary.exception.ResourceNotFoundException;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import com.daniel.projects.booklibrary.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AuthorService {
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final AuthorResponseDTOMapper authorMapper;
	private final CacheService cacheService;
	private final static String notFoundMessage = "Author not found with ";

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);

	public List<AuthorResponseDTO> findAllAuthors() {

		return authorRepository.findAll().stream().map(authorMapper).toList();
	}

	public Author addAuthor(final AuthorNameDTO authorName) {
		if (authorRepository.existsByName(authorName.getName())) {
			throw new ResourceAlreadyExistsException("Author with this name already exists");
		}
		Author author = new Author();
		author.setName(authorName.getName());
		return authorRepository.save(author);
	}

	public List<Author> addAuthors(final List<AuthorNameDTO> authorNames) {
		List<Author> authors = authorNames.stream()
				.filter(authorNameDTO -> !authorRepository.existsByName(authorNameDTO.getName())).map(authorNameDTO -> {
					Author author = new Author();
					author.setName(authorNameDTO.getName());
					return author;
				}).toList();
		return authorRepository.saveAll(authors);
	}

	public AuthorResponseDTO findByName(final String name) {
		Author author = authorRepository.findAuthorByName(name)
				.orElseThrow(() -> new ResourceNotFoundException(notFoundMessage + name));
		return authorMapper.apply(author);
	}

	public void updateAuthorName(final Long id, final String newName) {
		Author existingAuthor = authorRepository.findAuthorById(id)
				.orElseThrow(() -> new ResourceNotFoundException(notFoundMessage + id));
		existingAuthor.setName(newName);
		authorRepository.save(existingAuthor);
		cacheService.updateAuthor(existingAuthor);
	}

	public AuthorResponseDTO findAuthorById(final Long id) {
		Author author = cacheService.getAuthor(id);

		if (author == null) {
			Author retrievedAuthor = authorRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(notFoundMessage + id));

			cacheService.addAuthor(retrievedAuthor);
			LOGGER.info("Author retrieved from " + "repository and added to cache");
			return authorMapper.apply(retrievedAuthor);
		} else {
			LOGGER.info("Author retrieved from cache");
		}

		return authorMapper.apply(author);
	}
	public void deleteAuthorById(final Long id) {
		Author author = authorRepository.findAuthorById(id)
				.orElseThrow(()-> new ResourceNotFoundException(notFoundMessage + id));
		for (Book book : author.getBooks()) {
			book.getAuthors().remove(author);
			bookRepository.save(book);
		}
		authorRepository.delete(author);
		cacheService.removeAuthor(author.getId());
	}



}
