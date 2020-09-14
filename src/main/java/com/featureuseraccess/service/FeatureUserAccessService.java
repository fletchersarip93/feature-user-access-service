package com.featureuseraccess.service;

public interface FeatureUserAccessService {
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException;
	public void configureAccess(String userEmail, String featureName, boolean enable) throws UpdateFailedException;
}
