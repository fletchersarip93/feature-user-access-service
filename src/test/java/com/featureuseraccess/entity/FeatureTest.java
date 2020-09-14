package com.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class FeatureTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testAllowMethodsOfFeature() {
		User user1 = new User();
		user1.setId(1);
		User user2 = new User();
		user2.setId(2);
		Feature feature = new Feature();
		
		feature.allowUser(user1);
		feature.allowUser(user2);
		
		assertThat(feature.checkAllows(user1)).isTrue();
		assertThat(feature.checkAllows(user2)).isTrue();
		
		feature.disallowUser(user2);
		
		assertThat(feature.checkAllows(user2)).isFalse();
	}
	
	@Test
	public void testThatTheNameColumnIsCaseInsensitiveAndUnique() {
		Feature feature = new Feature();
		feature.setName("test");
		entityManager.persist(feature);
		
		Feature feature2 = new Feature();
		feature2.setName("TEST");
		assertThrows(PersistenceException.class, ()->entityManager.persist(feature2));
	}

}
