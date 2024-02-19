package com.daniel.projects.booklibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

@Data
@Entity
@Table(name = "booklibrary")
public class Book
{
	@Id
	@GeneratedValue
	private Long id;

	private String title;
	private String author;
	private String publisher;
	private double price;



}
