package com.featureuseraccess.service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	public static final String TOPIC_FEATURE_USER_ACCESS = "featureUserAccess";

	@Bean
	public NewTopic topicFeatureUserAccess(KafkaAdmin admin) {
		return TopicBuilder.name(TOPIC_FEATURE_USER_ACCESS)
				.partitions(3)
				.replicas(1)
				.build();
	}
	
}
