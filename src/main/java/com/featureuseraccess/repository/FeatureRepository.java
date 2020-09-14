package com.featureuseraccess.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.featureuseraccess.entity.Feature;

/**
 * Repository for Feature entities.
 * 
 * @author Fletcher Sarip
 *
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
	
	/**
	 * Find feature by name ignoring case.
	 * @param name Name of the feature to be found.
	 * @return Feature with the given name.
	 */
	Optional<Feature> findByNameIgnoreCase(String name);

}
