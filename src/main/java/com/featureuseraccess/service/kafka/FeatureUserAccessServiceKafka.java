package com.featureuseraccess.service.kafka;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.featureuseraccess.dto.FeatureUserAccessDto;
import com.featureuseraccess.entity.Feature;
import com.featureuseraccess.entity.User;
import com.featureuseraccess.repository.FeatureRepository;
import com.featureuseraccess.repository.UserRepository;
import com.featureuseraccess.service.ErrorMessageFactory;
import com.featureuseraccess.service.FeatureUserAccessService;
import com.featureuseraccess.service.ResourceNotFoundException;
import com.featureuseraccess.service.UpdateFailedException;

@Service
public class FeatureUserAccessServiceKafka implements FeatureUserAccessService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private KafkaTemplate<String, FeatureUserAccessDto> kafkaTemplate;

	@Override
	public boolean checkAccess(String userEmail, String featureName) throws ResourceNotFoundException {
		// read from repository
		Feature feature = featureRepository.findByNameIgnoreCase(featureName).orElseThrow(()->new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(featureName)));
		User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow(()->new ResourceNotFoundException(ErrorMessageFactory.userEmailNotFound(userEmail)));
		return feature.checkAllows(user);
	}

	@Override
	public void configureAccess(String userEmail, String featureName, boolean enable) throws UpdateFailedException {
		try {
			// validate
			featureRepository.findByNameIgnoreCase(featureName)
			.orElseThrow(()->new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(featureName)));
			userRepository.findByEmailIgnoreCase(userEmail)
			.orElseThrow(()->new ResourceNotFoundException(ErrorMessageFactory.userEmailNotFound(userEmail)));
			
			// post message to Kafka
			ListenableFuture<SendResult<String,FeatureUserAccessDto>> listenableFuture = this.kafkaTemplate.send(KafkaTopicConfig.TOPIC_FEATURE_USER_ACCESS, new FeatureUserAccessDto(featureName, userEmail, enable));
			listenableFuture.get();
		} catch (InterruptedException | ExecutionException | ResourceNotFoundException e) {
			throw new UpdateFailedException(e);
		}
		
	}
	
	@KafkaListener(topics = KafkaTopicConfig.TOPIC_FEATURE_USER_ACCESS, groupId = "group1")
	public void listenFeatureUserAccess(FeatureUserAccessDto dto) throws ResourceNotFoundException {
		System.out.println("@@@ Processing message: " + dto.toString());
		
		Feature feature = featureRepository.findByNameIgnoreCase(dto.getFeatureName()).orElseThrow(()->new ResourceNotFoundException());
		User user = userRepository.findByEmailIgnoreCase(dto.getEmail()).orElseThrow(()-> new ResourceNotFoundException());
		
		if (dto.getEnable()) {
			feature.allowUser(user);
		} else {
			feature.disallowUser(user);
		}
		
		featureRepository.saveAndFlush(feature);
	}

}
