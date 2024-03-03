package com.daniel.projects.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PublisherDTO {
	private String name;
	private List<BookDTO> books;
	private String address;
}
