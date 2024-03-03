package com.daniel.projects.booklibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "book")
public class Book
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String author;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "publisher_id")
	@JsonIgnoreProperties("book")
	private Publisher publisher;

	private double price;
}
