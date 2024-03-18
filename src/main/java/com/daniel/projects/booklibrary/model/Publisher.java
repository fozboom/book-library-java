package com.daniel.projects.booklibrary.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "publisher")
public class Publisher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "publisher", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Book> books = new ArrayList<>();

	private String address;

	public void addBook(Book book) {
		if (this.books == null) {
			this.books = new ArrayList<>();
		}
		this.books.add(book);
	}

	public void removeBook(Book book) {
		if (this.books != null) {
			this.books.remove(book);
		}
	}
}
