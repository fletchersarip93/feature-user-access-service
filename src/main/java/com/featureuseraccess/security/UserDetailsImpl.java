package com.featureuseraccess.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.featureuseraccess.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 2162629856778035415L;

	private final User user;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities()
				.stream()
				.map((authority) -> new SimpleGrantedAuthority(authority.getAuthority()))
				.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return !user.isAccountExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !user.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !user.isCredentialsExpired();
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

}
