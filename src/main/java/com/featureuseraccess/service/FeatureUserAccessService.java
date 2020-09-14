package com.featureuseraccess.service;

/**
 * Service for handling access permission of a certain user for a certain feature.
 * 
 * @author Fletcher Sarip
 *
 */
public interface FeatureUserAccessService {
	
	/**
	 * Check whether the user with the given email has access to the feature with the given name.
	 * @param userEmail Email of the user to be checked.
	 * @param featureName Name of the feature to be checked.
	 * @return Boolean value specifying whether the user has access to the feature.
	 * @throws ResourceNotFoundException If user with the given email or feature with the given name cannot be found.
	 */
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException;
	
	/**
	 * Configure whether the user with the given email can access the feature with the given name.
	 * @param userEmail Email of the user whose access to be configured.
	 * @param featureName Name of the feature whose access to be configured.
	 * @param enable Give true to allow access and false to disallow access.
	 * @throws UpdateFailedException If user with the given email or feature with the given name cannot be found, or
	 * any other error occurred on the update mechanism.
	 */
	public void configureAccess(String userEmail, String featureName, boolean enable) throws UpdateFailedException;
	
}
