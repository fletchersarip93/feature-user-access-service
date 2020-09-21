package com.featureuseraccess.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.featureuseraccess.dto.FeatureUserAccessDto;
import com.featureuseraccess.service.FeatureUserAccessService;
import com.featureuseraccess.service.ResourceNotFoundException;
import com.featureuseraccess.service.UpdateFailedException;

/**
 * REST controller to expose the Feature User Access Service.
 * 
 * @author Fletcher Sarip
 *
 */
@RestController
@RequestMapping("/feature")
@Validated
public class FeatureUserAccessRestController {
	
	@Autowired
	private FeatureUserAccessService featureUserAccessService;
	
	/**
	 * Get the access permission of the given user email for the given feature name.
	 * @param email Email of the user.
	 * @param featureName Name of the feature.
	 * @return Key-value pair, where the key is "canAccess", which indicates whether the user has access to the feature.
	 * @throws ResourceNotFoundException If user with the given email or feature with the given name cannot be found.
	 */
	@GetMapping
	public Map<String, Boolean> getFeatureAccess (
			@RequestParam(required = true)
			@NotBlank
			@Email
			String email,
			@RequestParam(required = true)
			@NotBlank
			String featureName) throws ResourceNotFoundException {
		return Collections.singletonMap("canAccess", featureUserAccessService.checkAccess(email, featureName));
	}
	
	/**
	 * Configure access permission of a user for a feature.
	 * @param featureUserAccessDto Data transfer object containing the details of the access-permission to be configured.
	 * @throws UpdateFailedException If user with the given email or feature with the given name cannot be found, or any other possible
	 * error on the update mechanism.
	 */
	@PostMapping
	public void addFeatureUserAccess(
			@RequestBody
			@Valid
			FeatureUserAccessDto featureUserAccessDto) throws UpdateFailedException {
		featureUserAccessService.configureAccess(featureUserAccessDto.getEmail(), featureUserAccessDto.getFeatureName(), featureUserAccessDto.getEnable());
	}

}
