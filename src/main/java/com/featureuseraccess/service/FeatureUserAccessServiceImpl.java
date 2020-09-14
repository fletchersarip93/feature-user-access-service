package com.featureuseraccess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.featureuseraccess.entity.Feature;
import com.featureuseraccess.entity.User;
import com.featureuseraccess.repository.FeatureRepository;
import com.featureuseraccess.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class FeatureUserAccessServiceImpl implements FeatureUserAccessService {
	
	@Autowired
	private FeatureRepository featureRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException {
		Feature feature = findFeatureByName(featureName);
		User user = findUserByEmail(userEmail);
		boolean canAccess = feature.checkAllows(user);
		return canAccess;
	}
	
	public void configureAccess(String userEmail, String featureName, boolean enable) throws ResourceNotFoundException {
		Feature feature = findFeatureByName(featureName);
		User user = findUserByEmail(userEmail);
		
		if (enable) { 
			feature.allowUser(user);
		} else {
			feature.disallowUser(user);
		}
		
		featureRepository.save(feature);
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
