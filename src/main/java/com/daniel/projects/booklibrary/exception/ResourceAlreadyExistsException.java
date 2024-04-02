package com.daniel.projects.booklibrary.exception;

import java.io.Serial;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(final String message) {
		super(message);
	}
}
