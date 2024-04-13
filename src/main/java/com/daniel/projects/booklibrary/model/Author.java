package com.daniel.projects.booklibrary.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "author")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Author name is mandatory")
	@Size(min = 3, max = 50, message = "Author name must be between 3 and 50 characters")
	@Column(unique = true)
	private String name;

	@ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
	private List<Book> books = new ArrayList<>();

	public void addBook(final Book book) {
		if (this.books == null) {
			this.books = new ArrayList<>();
		}
		this.books.add(book);
	}

	public void removeBook(final Book book) {
		if (this.books != null) {
			this.books.remove(book);
		}
	}

	@Override
	public String toString() {
		return "Author{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

