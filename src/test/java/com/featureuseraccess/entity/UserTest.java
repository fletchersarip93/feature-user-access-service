package com.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserTest {

	@Autowired
	private TestEntityManager entityManager;

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
	
	@Test
	public void testThatTheEmailColumnIsCaseInsensitiveAndUnique() {
		User user = new User();
		user.setEmail("test@domain.com");
		user.setPassword("password");
		entityManager.persist(user);
		
		User user2 = new User();
		user2.setEmail("TeSt@dOmAiN.CoM");
		user2.setPassword("password");
		assertThrows(PersistenceException.class, ()->entityManager.persist(user2));
	}

	private List<String> getViolationStringList(Set<ConstraintViolation<User>> violations) {
		return violations.stream()
				.map((violation) -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.toList());
	}

}
