package com.featureuseraccess.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.featureuseraccess.entity.User;

/**
 * Repository for User entities.
 * 
 * @author Fletcher Sarip
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * Find user with the given email ignoring case.
	 * @param email The email of the user to be found.
	 * @return User with the given email.
	 */
	Optional<User> findByEmailIgnoreCase(String email);
	
}
