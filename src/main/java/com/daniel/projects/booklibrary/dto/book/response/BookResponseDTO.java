package com.daniel.projects.booklibrary.dto.book.response;

import com.daniel.projects.booklibrary.dto.publisher.withoutbooks.PublisherWithoutBooksDTO;
import com.daniel.projects.booklibrary.dto.author.name.AuthorNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
	private Long id;
	private String title;
	private List<AuthorNameDTO> authors;
	private PublisherWithoutBooksDTO publisher;
	private Double price;
}

