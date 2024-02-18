package com.daniel.projects.booklibrary.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Book
{
	private static int nextId = 1;
	private final int id;
	private String name;
	private String author;
	private double price;

	public Book()
	{
		this.id = nextId++;
	}

	public Book(String name, String author, double price)
	{
		this();
		this.name = name;
		this.author = author;
		this.price = price;
	}



}
