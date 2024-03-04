package com.daniel.projects.booklibrary.dto.publisher.response;

import com.daniel.projects.booklibrary.dto.book.title.BookTitleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherResponseDTO {
	private Long id;
	private String name;
	private List<BookTitleDTO> books;
	private String address;
}
