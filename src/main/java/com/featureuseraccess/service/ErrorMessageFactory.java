package com.featureuseraccess.service;

/**
 * Helper factory class to construct error messages.
 * 
 * @author Fletcher Sarip
 *
 */
public class ErrorMessageFactory {

	/**
	 * Construct error message for the case when a user with the given email cannot be found.
	 * @param userEmail The user email that cannot be found.
	 * @return Error message specifying that user with the given email cannot be found.
	 */
	static public String userEmailNotFound(String userEmail) {
		return "User with email '" + userEmail + "' cannot be found.";
	}

	/**
	 * Construct error message for the case when a feature with the given name cannot be found.
	 * @param featureName The feature name that cannot be found.
	 * @return Error message specifying that feature with the given name cannot be found.
	 */
	static public String featureNameNotFound(String featureName) {
		return "Feature with name '" + featureName + "' cannot be found.";
	}
	
}
