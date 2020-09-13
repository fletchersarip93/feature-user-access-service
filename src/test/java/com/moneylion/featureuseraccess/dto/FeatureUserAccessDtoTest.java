package com.moneylion.featureuseraccess.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class FeatureUserAccessDtoTest {

	@Test
	void testFeatureNameValidation() {
		FeatureUserAccessDto dto = new FeatureUserAccessDto();
		dto.setEmail("user1@emaildomain.com");
		dto.setEnable(true);
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<FeatureUserAccessDto>> violations = validator.validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream().map((v) -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList()))
		.contains("featureName: must not be blank");
		
		dto.setFeatureName("");
		
		violations = validator.validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream().map((v) -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList()))
		.contains("featureName: must not be blank");
		
		dto.setFeatureName(" ");
		
		violations = validator.validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream().map((v) -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList()))
		.contains("featureName: must not be blank");
		
		dto.setFeatureName("abc");
		
		violations = validator.validate(dto);
		assertThat(violations.size()).isEqualTo(0);
	}
	
	@Test
	void testEmailValidation() {
		FeatureUserAccessDto dto = new FeatureUserAccessDto();
		dto.setFeatureName("featurename");
		dto.setEnable(true);
		
		dto.setEmail("fdfds");
		Set<ConstraintViolation<FeatureUserAccessDto>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList())).contains("email: must be a well-formed email address");
		
		dto.setEmail("");
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList())).contains("email: must not be blank");
		
		dto.setEmail(" ");
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList())).contains("email: must not be blank");
		
		dto.setEmail("user@emaildomain.com");
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isEqualTo(0);
	}
	
	@Test
	void testEnableValidation() {
		FeatureUserAccessDto dto = new FeatureUserAccessDto();
		dto.setFeatureName("featurename");
		dto.setEmail("user@emaildomain.com");
		
		dto.setEnable(null);
		Set<ConstraintViolation<FeatureUserAccessDto>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isGreaterThan(0);
		assertThat(violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList())).contains("enable: must not be null");
		
		dto.setEnable(true);
		violations = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		assertThat(violations.size()).isEqualTo(0);
	}

}
