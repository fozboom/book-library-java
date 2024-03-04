package com.daniel.projects.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
	private String title;
	private List<AuthorNameDTO> authors;
	private PublisherWithoutBooksDTO publisher;
	private Double price;
}
