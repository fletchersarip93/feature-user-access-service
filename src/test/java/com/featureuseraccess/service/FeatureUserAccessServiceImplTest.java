package com.featureuseraccess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.featureuseraccess.entity.Feature;
import com.featureuseraccess.entity.User;
import com.featureuseraccess.repository.FeatureRepository;
import com.featureuseraccess.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class FeatureUserAccessServiceImplTest {
	
	private static final String FEATURE_NAME = "inventory-management-feature";
	private static final String FEATURE_NAME_NOT_ALLOWED = "very-important-feature-name";
	private static final String USER_EMAIL = "user1@emaildomain.com";
	private static final String USER_EMAIL_NOT_ALLOWED = "user2@emaildomain.com";
	
	@TestConfiguration
    static class TestConfig {
		
        @Bean
        public FeatureUserAccessService employeeService() {
            return new FeatureUserAccessServiceImpl();
        }
        
    }
	
	@MockBean
	private FeatureRepository featureRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	private FeatureUserAccessService service;
	
	@BeforeEach
	public void initMock() {
		User user = new User();
		user.setId(1);
		user.setEmail(USER_EMAIL);
		User userNotAllowed = new User();
		userNotAllowed.setId(2);
		userNotAllowed.setEmail(USER_EMAIL_NOT_ALLOWED);
		Feature feature = new Feature();
		feature.setId(1);
		feature.setName(FEATURE_NAME);
		feature.allowUser(user);
		Feature featureVeryImportant = new Feature();
		featureVeryImportant.setId(2);
		featureVeryImportant.setName(FEATURE_NAME_NOT_ALLOWED);
		
		when(featureRepository.findByNameIgnoreCase(argThat(equalToIgnoringCase(FEATURE_NAME))))
				.thenReturn(Optional.of(feature));
		when(featureRepository.findByNameIgnoreCase(argThat(equalToIgnoringCase(FEATURE_NAME_NOT_ALLOWED))))
				.thenReturn(Optional.of(featureVeryImportant));
		when(featureRepository.save(feature)).thenReturn(feature);
		
		when(userRepository.findByEmailIgnoreCase(argThat(equalToIgnoringCase(USER_EMAIL))))
				.thenReturn(Optional.of(user));
		when(userRepository.findByEmailIgnoreCase(argThat(equalToIgnoringCase(USER_EMAIL_NOT_ALLOWED))))
				.thenReturn(Optional.of(userNotAllowed));
	}

	@Test
	void checkAccessOnAllowedUserAndAllowedFeatureShouldReturnTrue() throws ResourceNotFoundException {
		assertThat(service.checkAccess(USER_EMAIL, FEATURE_NAME)).isTrue();
	}
	
	@Test
	void checkAccessOnNotAllowedUserShouldReturnFalse() throws ResourceNotFoundException {
		assertThat(service.checkAccess(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME)).isFalse();
	}
	
	@Test
	void checkAccessOnNotAllowedFeatureShouldReturnFalse() throws ResourceNotFoundException {
		assertThat(service.checkAccess(USER_EMAIL, FEATURE_NAME_NOT_ALLOWED)).isFalse();
	}

	@Test
	void configureAccessOnExistingUserEmailAndFeatureNameShouldNotThrowException() throws UpdateFailedException {
		// just call the method and expect that no exception is thrown
		service.configureAccess(USER_EMAIL, FEATURE_NAME, true);
	}
	
	@Test
	void configureAccessOnNonExistentUserEmailShouldThrowException() throws UpdateFailedException {
		String nonExistentEmail = "nonexistentuser@emaildomain.com";
		UpdateFailedException exception = assertThrows(UpdateFailedException.class, ()->{
			service.configureAccess(nonExistentEmail, FEATURE_NAME, true);
		});
		assertThat(exception.getMessage()).contains("User with email '" + nonExistentEmail + "' cannot be found.");
	}
	
	@Test
	void configureAccessOnNonExistentFeatureNameShouldThrowException() throws UpdateFailedException {
		String nonExistentFeatureName = "non-existent-feature";
		UpdateFailedException exception = assertThrows(UpdateFailedException.class, ()->{
			service.configureAccess(USER_EMAIL, nonExistentFeatureName, true);
		});
		assertThat(exception.getMessage()).contains("Feature with name '" + nonExistentFeatureName + "' cannot be found.");
	}

}
