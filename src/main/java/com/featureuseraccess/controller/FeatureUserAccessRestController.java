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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class FeatureUserAccessRestController {
	
	private final FeatureUserAccessService featureUserAccessService;
	
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
	
	@PostMapping
	public void addFeatureUserAccess(
			@RequestBody
			@Valid
			FeatureUserAccessDto featureUserAccessDto) throws UpdateFailedException {
		try {
			featureUserAccessService.configureAccess(featureUserAccessDto.getEmail(), featureUserAccessDto.getFeatureName(), featureUserAccessDto.getEnable());
		} catch (ResourceNotFoundException e) {
			throw new UpdateFailedException(e);
		}
	}

}
