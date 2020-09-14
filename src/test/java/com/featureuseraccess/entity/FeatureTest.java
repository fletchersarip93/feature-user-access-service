package com.featureuseraccess.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

import org.junit.jupiter.api.Test;

class FeatureTest {

	@Test
	void testAllowMethodsOfFeature() {
		User user1 = new User();
		user1.setId(1);
		User user2 = new User();
		user2.setId(2);
		Feature feature = new Feature();
		
		feature.allowUser(user1);
		feature.allowUser(user2);
		
		assertThat(feature.checkAllows(user1));
		assertThat(feature.checkAllows(user2));
		
		feature.disallowUser(user2);
		
		assertThat(not(feature.checkAllows(user2)));
	}

}
