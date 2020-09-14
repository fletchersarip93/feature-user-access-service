package com.featureuseraccess.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorMessageFactoryTest {

	@Test
	void testUserEmailNotFound() {
		String userEmail = "abc";
		assertThat(ErrorMessageFactory.userEmailNotFound(userEmail))
		.isEqualTo("User with email '" + userEmail + "' cannot be found.");
	}

	@Test
	void testFeatureNameNotFound() {
		String featureName = "abc";
		assertThat(ErrorMessageFactory.featureNameNotFound(featureName))
		.isEqualTo("Feature with name '" + featureName + "' cannot be found.");
	}

}
