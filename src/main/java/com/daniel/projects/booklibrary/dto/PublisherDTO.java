package com.daniel.projects.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDTO {
	private String name;
	private List<BookResponseDTO> books;
	private String address;
}
