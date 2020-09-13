package com.moneylion.featureuseraccess.service;

import com.moneylion.featureuseraccess.controller.exception.ResourceNotFoundException;

public interface FeatureUserAccessService {
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException;
	public void configureAccess(String userEmail, String featureName, boolean enable) throws ResourceNotFoundException;
}
