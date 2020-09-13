package com.moneylion.featureuseraccess.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Feature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true)
	@NotBlank
	private String name;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<User> usersAllowed = new ArrayList<>();

	public boolean checkAllows(User user) {
		return this.usersAllowed.contains(user);
	}

	public void allowUser(User user) {
		this.usersAllowed.add(user);
	}

	public void disallowUser(User user) {
		this.usersAllowed.remove(user);
	}
}
