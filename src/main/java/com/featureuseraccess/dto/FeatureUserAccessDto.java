package com.featureuseraccess.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for feature user access configuration.
 * 
 * @author Fletcher Sarip
 *
 */
@Data
@EqualsAndHashCode
public class FeatureUserAccessDto {

	@NotBlank
	private String featureName;
	
	@NotBlank
	@Email
	private String email;
	
	@NotNull
	private Boolean enable;
}
