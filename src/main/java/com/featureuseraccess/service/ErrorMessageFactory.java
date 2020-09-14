package com.featureuseraccess.service;

public class ErrorMessageFactory {

	static public String userEmailNotFound(String userEmail) {
		return "User with email '" + userEmail + "' cannot be found.";
	}

	static public String featureNameNotFound(String featureName) {
		return "Feature with name '" + featureName + "' cannot be found.";
	}
	
}
