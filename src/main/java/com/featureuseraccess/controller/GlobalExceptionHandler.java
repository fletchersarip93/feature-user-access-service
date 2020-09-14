package com.featureuseraccess.controller;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.featureuseraccess.service.ResourceNotFoundException;
import com.featureuseraccess.service.UpdateFailedException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiError handleConstraintViolationException(ConstraintViolationException ex) {
		return new ApiError(
				ex.getConstraintViolations()
				.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ApiError handleResourceNotFoundException(ResourceNotFoundException ex) {
		return constructApiError(ex);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity
				.status(status)
				.body(constructApiError(ex));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity
				.status(status)
				.body(new ApiError(
						ex.getBindingResult().getFieldErrors()
						.stream()
						.map((error) -> error.getField() + ": " + error.getDefaultMessage())
						.collect(Collectors.toList())));
	}
	
	@ExceptionHandler(UpdateFailedException.class)
	@ResponseStatus(HttpStatus.NOT_MODIFIED)
	protected ApiError handleUpdateFailedException(UpdateFailedException ex) {
		return constructApiError(ex);
	}
	
	private ApiError constructApiError(Throwable ex) {
		return new ApiError(Collections.singletonList(ex.getMessage()));
	}
	

}
