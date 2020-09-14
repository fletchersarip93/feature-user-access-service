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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Entity to represent User.
 * 
 * @author Fletcher Sarip
 *
 */
@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true, columnDefinition = "VARCHAR_IGNORECASE")
	@Email
	@NotBlank
	private String email;
	
	@Column(nullable = false)
	@NotBlank
	private String password;
	
	private boolean enabled;
	
	private boolean expired;
	
	private boolean accountExpired;
	
	private boolean credentialsExpired;
	
	private boolean locked;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_authority",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private List<Authority> authorities = new ArrayList<>();

	/**
	 * Adds the given authority to this user.
	 * @param auth The authority to be given to this user.
	 */
	public void addAuthority(Authority auth) {
		this.authorities.add(auth);
	}
	
}
