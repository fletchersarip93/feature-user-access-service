package com.featureuseraccess.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourceNotFoundException extends Exception {
	
	private static final long serialVersionUID = -8613586731635490498L;

	@Getter
	private final String message;

}
