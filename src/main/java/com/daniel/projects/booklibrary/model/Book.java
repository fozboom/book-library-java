package com.daniel.projects.booklibrary.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	private String title;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "author_id"))
	@JsonIgnoreProperties("books")
	private List<Author> authors = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "publisher_id")
	@JsonIgnoreProperties("book")
	private Publisher publisher;

	private double price;
}
