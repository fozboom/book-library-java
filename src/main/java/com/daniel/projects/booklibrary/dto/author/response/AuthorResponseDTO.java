package com.daniel.projects.booklibrary.dto.author.response;

import com.daniel.projects.booklibrary.dto.book.title.BookTitleDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseDTO {
	private Long id;
	@NotBlank(message = "Author name is mandatory")
	@Size(min = 3, max = 50, message = "Author name must be between 3 and 50 characters")
	private String name;
	private List<BookTitleDTO> books;
}

