package com.moneylion.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.Test;

class AuthorityTest {

	@Test
	void testAuthorityValidation() {
		Authority auth = new Authority();
		Set<ConstraintViolation<Authority>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(auth);
		assertThat(violations).hasSize(1);
		assertThat(violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList())).contains("authority: must not be blank");
		
		auth.setAuthority("someauth");
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(auth);
		assertThat(violations.size()).isEqualTo(0);
	}

}
