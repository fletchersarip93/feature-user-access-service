package com.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void testUserEmailAndPasswordValidation() {
		User user = new User();
		user.setEmail(" ");
		Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
		assertThat(violations.size()).isEqualTo(3);
		assertThat(getViolationStringList(violations)).contains("email: must not be blank");
		assertThat(getViolationStringList(violations)).contains("email: must be a well-formed email address");
		assertThat(getViolationStringList(violations)).contains("password: must not be blank");
		
		user.setEmail("fdfds");
		
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
		assertThat(violations.size()).isEqualTo(2);
		assertThat(getViolationStringList(violations)).contains("email: must be a well-formed email address");
		assertThat(getViolationStringList(violations)).contains("password: must not be blank");
		
		
		user.setEmail("user@emaildomain.com");
		user.setPassword("password");
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
		assertThat(violations.size()).isEqualTo(0);
	}

	private List<String> getViolationStringList(Set<ConstraintViolation<User>> violations) {
		return violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList());
	}

}
