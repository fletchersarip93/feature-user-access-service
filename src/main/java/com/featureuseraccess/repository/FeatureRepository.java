package com.featureuseraccess.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.featureuseraccess.entity.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
	
	Optional<Feature> findByNameIgnoreCase(String name);

}