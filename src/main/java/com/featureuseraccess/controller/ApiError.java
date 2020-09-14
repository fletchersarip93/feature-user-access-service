package com.featureuseraccess.controller;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiError {
	
	@Getter
	private final List<String> messages;

}
