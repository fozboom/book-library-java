package com.daniel.projects.booklibrary.dto.publisher.withoutbooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherWithoutBooksDTO {
	private Long id;
	private String name;
	private String address;
}
