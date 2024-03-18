package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTOMapper;
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

	private static final Logger logger = LoggerFactory.getLogger(BookService.class);


	public List<AuthorResponseDTO> findAllAuthors() {

		return authorRepository.findAll().stream().map(authorMapper).toList();
	}


	public Optional<Author> addAuthor(Author author) {
		if (authorRepository.existsByName(author.getName())) {
			return Optional.empty();
		}
		cacheService.addAuthor(author);
		return Optional.of(authorRepository.save(author));
	}


	public AuthorResponseDTO findByName(String name) {

		Author author = authorRepository.findAuthorByName(name);
		if (author == null) {
			return null;
		}
		return authorMapper.apply(author);
	}


	public boolean updateAuthorName(Long id, String newName) {
		Optional<Author> existingAuthorOptional = authorRepository.findById(id);
		if (existingAuthorOptional.isEmpty()) {
			return false;
		}

		Author existingAuthor = existingAuthorOptional.get();
		existingAuthor.setName(newName);
		authorRepository.save(existingAuthor);
		cacheService.updateAuthor(existingAuthor);
		return true;
	}

	public AuthorResponseDTO findAuthorById(Long id) {
		Author author = cacheService.getAuthor(id);

		if (author == null) {
			Optional<Author> optionalAuthor = authorRepository.findById(id);

			if (optionalAuthor.isEmpty()) {
				return null;
			}

			Author retrievedAuthor = optionalAuthor.get();

			cacheService.addAuthor(retrievedAuthor);
			logger.info("Author retrieved from repository and added to cache");
			return authorMapper.apply(retrievedAuthor);
		} else {
			logger.info("Author retrieved from cache");
		}

		return authorMapper.apply(author);
	}


	public boolean deleteAuthorByName(String name) {
		Author author = authorRepository.findByName(name);
		if (author != null) {
			for (Book book : author.getBooks()) {
				book.getAuthors().remove(author);
				bookRepository.save(book);
			}
			authorRepository.delete(author);
			cacheService.removeAuthor(author.getId());
			return true;
		}
		return false;
	}
}