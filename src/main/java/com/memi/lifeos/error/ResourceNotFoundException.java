package com.memi.lifeos.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

	public ResourceNotFoundException(String resource, Object id) {
		super(HttpStatus.NOT_FOUND, resource + " not found: " + id);
	}
}
