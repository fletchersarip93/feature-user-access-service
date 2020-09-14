package com.featureuseraccess.controller;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error response body for the API, to contain a list of error messages.
 * 
 * @author Fletcher Sarip
 *
 */
@RequiredArgsConstructor
public class ApiError {
	
	@Getter
	private final List<String> messages;

}
