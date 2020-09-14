package com.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
class AuthorityTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testAuthorityValidation() {
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
	
	@Test
	public void testThatTheAuthorityColumnIsCaseInsensitiveAndUnique() {
		Authority auth = new Authority();
		auth.setAuthority("test");
		entityManager.persist(auth);
		
		Authority auth2 = new Authority();
		auth2.setAuthority("TEST");
		assertThrows(PersistenceException.class, ()->entityManager.persist(auth2));
	}

}
