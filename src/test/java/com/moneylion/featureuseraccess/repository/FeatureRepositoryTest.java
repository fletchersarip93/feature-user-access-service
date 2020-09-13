package com.moneylion.featureuseraccess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.moneylion.featureuseraccess.entity.Feature;

@DataJpaTest
class FeatureRepositoryTest {
	
	private static final String FEATURE_NAME = "feature-name-1";

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private FeatureRepository featureRepository;

	private boolean isInitialized;

	@BeforeEach
	public void initEntities() {
		if (!isInitialized) {
			Feature feature = new Feature();
			feature.setName(FEATURE_NAME);
			entityManager.persistAndFlush(feature);
			isInitialized = true;
		}
	}
	
	@Test
	void findByNameIgnoreCaseShouldIgnoreCaseOfFeatureName() {
		Optional<Feature> featureOptional = featureRepository.findByNameIgnoreCase(FEATURE_NAME.toLowerCase());
		
		assertThat(featureOptional.isPresent());
		assertThat(featureOptional.get().getName()).isEqualTo(FEATURE_NAME);
		
		featureOptional = featureRepository.findByNameIgnoreCase(FEATURE_NAME.toUpperCase());
		
		assertThat(featureOptional.isPresent());
		assertThat(featureOptional.get().getName()).isEqualTo(FEATURE_NAME);
	}

}
