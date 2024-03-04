package com.daniel.projects.booklibrary.model;



import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "book")
public class Book
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(unique=true)
	private String title;

	@ManyToMany
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "author_id"))

	private List<Author> authors = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	private Double price;
}
