package com.featureuseraccess.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.featureuseraccess.entity.Authority;
import com.featureuseraccess.entity.User;

class UserDetailsImplTest {

	@Test
	void test() {
		User user = new User();
		user.setId(1);
		user.setEmail("f@gmail.com");
		user.setEnabled(true);
		user.setAccountExpired(true);
		user.setCredentialsExpired(true);
		user.setExpired(true);
		user.setLocked(true);
		user.setPassword("password");
		
		Authority auth1 = new Authority();
		auth1.setId(1);
		auth1.setAuthority("product_manager");
		
		Authority auth2 = new Authority();
		auth2.setId(1);
		auth2.setAuthority("developer");
		
		user.setAuthorities(Arrays.asList(auth1, auth2));
		
		UserDetailsImpl userDetails = new UserDetailsImpl(user);
		
		assertThat(userDetails.getUsername()).isEqualTo("f@gmail.com");
		assertThat(userDetails.getPassword()).isEqualTo("password");
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		
		assertThat(authorities).hasSize(2);
		assertThat(authorities.stream()
				.map((auth)->auth.getAuthority())
				.collect(Collectors.toList())).contains("product_manager", "developer");
	}

}
