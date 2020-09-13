package com.moneylion.featureuseraccess.controller.exception;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiError {
	
	@Getter
	private final List<String> messages;

}
