package com.featureuseraccess.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.featureuseraccess.entity.Feature;
import com.featureuseraccess.entity.User;
import com.featureuseraccess.repository.FeatureRepository;
import com.featureuseraccess.repository.UserRepository;
import com.featureuseraccess.service.ErrorMessageFactory;
import com.featureuseraccess.service.FeatureUserAccessService;
import com.featureuseraccess.service.ResourceNotFoundException;
import com.featureuseraccess.service.UpdateFailedException;

/**
 * Implementation of the Feature User Access Service for handling access permission of a certain user for a certain feature.
 * 
 * @author Fletcher Sarip
 *
 */
@Service
@Primary
public class FeatureUserAccessServiceDB implements FeatureUserAccessService {
	
	@Autowired
	private FeatureRepository featureRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException {
		Feature feature = findFeatureByName(featureName);
		User user = findUserByEmail(userEmail);
		boolean canAccess = feature.checkAllows(user);
		return canAccess;
	}
	
	@Override
	public void configureAccess(String userEmail, String featureName, boolean enable) throws UpdateFailedException {
		try {
			Feature feature = findFeatureByName(featureName);
			User user = findUserByEmail(userEmail);
			
			if (enable) { 
				feature.allowUser(user);
			} else {
				feature.disallowUser(user);
			}
			
			featureRepository.save(feature);
		} catch (ResourceNotFoundException e) {
			throw new UpdateFailedException(e);
		}
	}
	
	private User findUserByEmail(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageFactory.userEmailNotFound(email)));
		return user;
	}

	private Feature findFeatureByName(String featureName) throws ResourceNotFoundException {
		Feature feature = featureRepository.findByNameIgnoreCase(featureName)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(featureName)));
		return feature;
	}
	
}
