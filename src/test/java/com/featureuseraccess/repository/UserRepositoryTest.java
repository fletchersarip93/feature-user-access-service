package com.featureuseraccess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.featureuseraccess.entity.User;

@DataJpaTest
class UserRepositoryTest {
	
	private static final String USER_EMAIL = "user1@emaildomain.com";

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;

	private boolean isInitialized;

	@BeforeEach
	public void initEntities() {
		if (!isInitialized) {
			User user = new User();
			user.setEmail(USER_EMAIL);
			user.setPassword("password");
			entityManager.persistAndFlush(user);
			isInitialized = true;
		}
	}
	
	@Test
	void findByEmailIgnoreCaseShouldIgnoreCaseOfEmail() {
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(USER_EMAIL.toLowerCase());
		
		assertThat(userOptional.isPresent());
		assertThat(userOptional.get().getEmail()).isEqualTo(USER_EMAIL);
		
		userOptional = userRepository.findByEmailIgnoreCase(USER_EMAIL.toUpperCase());
		
		assertThat(userOptional.isPresent());
		assertThat(userOptional.get().getEmail()).isEqualTo(USER_EMAIL);
	}

}
