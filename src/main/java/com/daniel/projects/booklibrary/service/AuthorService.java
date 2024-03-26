package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTOMapper;
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
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AuthorService {
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final AuthorResponseDTOMapper authorMapper;
	private final CacheService cacheService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);


	public List<AuthorResponseDTO> findAllAuthors() {

		return authorRepository.findAll().stream().map(authorMapper).toList();
	}


	public Author addAuthor(final Author author) {
		if (authorRepository.existsByName(author.getName())) {
			throw new ResourceAlreadyExistsException("Author with this name already exists");
		}
		cacheService.addAuthor(author);
		return authorRepository.save(author);
	}


	public AuthorResponseDTO findByName(final String name) {
		Author author = authorRepository.findAuthorByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with name: " + name));
		return authorMapper.apply(author);
	}


	public void updateAuthorName(final Long id, final String newName) {
		Author existingAuthor = authorRepository.findAuthorById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
		existingAuthor.setName(newName);
		authorRepository.save(existingAuthor);
		cacheService.updateAuthor(existingAuthor);
	}

	public AuthorResponseDTO findAuthorById(final Long id) {
		Author author = cacheService.getAuthor(id);

		if (author == null) {
			Author retrievedAuthor = authorRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));


			cacheService.addAuthor(retrievedAuthor);
			LOGGER.info("Author retrieved from " + "repository and added to cache");
			return authorMapper.apply(retrievedAuthor);
		} else {
			LOGGER.info("Author retrieved from cache");
		}

		return authorMapper.apply(author);
	}


	public void deleteAuthorByName(final String name) {
		Author author = authorRepository.findAuthorByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("Author not found with name: " + name));
		for (Book book : author.getBooks()) {
			book.getAuthors().remove(author);
			bookRepository.save(book);
		}
		authorRepository.delete(author);
		cacheService.removeAuthor(author.getId());
	}
}
