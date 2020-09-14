package com.featureuseraccess.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Entity to represent Feature.
 * 
 * @author Fletcher Sarip
 *
 */
@Entity
@Data
public class Feature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true, columnDefinition = "VARCHAR_IGNORECASE")
	@NotBlank
	private String name;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(
			name = "feature_user_access",
			joinColumns = @JoinColumn(name = "feature_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> usersAllowed = new ArrayList<>();

	/**
	 * Checks whether the given user is allowed to access this feature.
	 * @param user The user whose access permission to be checked.
	 * @return Boolean value on whether the user is allowed access to this feature.
	 */
	public boolean checkAllows(User user) {
		return this.usersAllowed.contains(user);
	}

	/**
	 * Allows the given user to access this feature.
	 * @param user The user to be allowed access to this feature.
	 */
	public void allowUser(User user) {
		this.usersAllowed.add(user);
	}

	/**
	 * Disallow the given user from accessing this feature.
	 * @param user The user to be disallowed from accessing this feature.
	 */
	public void disallowUser(User user) {
		this.usersAllowed.remove(user);
	}
}
