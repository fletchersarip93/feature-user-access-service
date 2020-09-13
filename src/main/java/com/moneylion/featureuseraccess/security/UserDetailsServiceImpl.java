package com.moneylion.featureuseraccess.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moneylion.featureuseraccess.entity.User;
import com.moneylion.featureuseraccess.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// username is email in this case
		User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(
				()->new UsernameNotFoundException(username));
		
		return new UserDetailsImpl(user);
	}
	
}
