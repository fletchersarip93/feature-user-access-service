package com.featureuseraccess.service;

public class ResourceNotFoundException extends Exception {
	
	private static final long serialVersionUID = -8613586731635490498L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
