package com.daniel.projects.booklibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	@Column(unique=true)
	private String name;

	@OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("publisher")
	private List<Book> books = new ArrayList<>();

	private String address;
}
