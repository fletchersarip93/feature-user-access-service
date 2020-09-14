package com.featureuseraccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Entity to represent user's authority.
 * 
 * @author Fletcher Sarip
 *
 */
@Entity
@Data
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true, columnDefinition = "VARCHAR_IGNORECASE")
	@NotBlank
	private String authority;
	
}
