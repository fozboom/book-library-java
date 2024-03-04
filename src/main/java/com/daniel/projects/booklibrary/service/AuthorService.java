package com.daniel.projects.booklibrary.service;

import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTO;
import com.daniel.projects.booklibrary.dto.author.response.AuthorResponseDTOMapper;
import com.daniel.projects.booklibrary.model.Author;
import com.daniel.projects.booklibrary.model.Book;
import com.daniel.projects.booklibrary.repository.AuthorRepository;
import com.daniel.projects.booklibrary.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final AuthorResponseDTOMapper mapper;

	@Transactional
	public List<AuthorResponseDTO> findAllAuthors() {

		return authorRepository.findAll().stream().map(mapper).toList();
	}

	@Transactional
	public Optional<Author> addAuthor(Author author) {
		if (authorRepository.existsByName(author.getName())) {
			return Optional.empty();
		}

		return Optional.of(authorRepository.save(author));
	}

	@Transactional
	public AuthorResponseDTO findByName(String name) {

		Author author = authorRepository.findByName(name);

		if (author == null) {
			return null;
		}

		return mapper.apply(author);
	}

	@Transactional
	public boolean updateAuthorName(Long id, String newName) {
		Optional<Author> existingAuthorOptional = authorRepository.findById(id);
		if (existingAuthorOptional.isEmpty()) {
			return false;
		}

		Author existingAuthor = existingAuthorOptional.get();
		existingAuthor.setName(newName);
		authorRepository.save(existingAuthor);

		return true;
	}

	@Transactional
	public boolean deleteAuthorByName (String name) {
		Author author = authorRepository.findByName(name);
		if (author != null) {
			for (Book book : author.getBooks()) {
				book.getAuthors().remove(author);
				if (book.getAuthors().isEmpty()) {
					bookRepository.delete(book);
				} else {
					bookRepository.save(book);
				}
			}
			authorRepository.delete(author);
			return true;
		}
		return false;
	}
}
