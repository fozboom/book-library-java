package com.daniel.projects.booklibrary.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(final String message) {
		super(message);
	}
}
