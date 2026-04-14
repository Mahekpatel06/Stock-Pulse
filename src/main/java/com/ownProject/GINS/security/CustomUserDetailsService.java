package com.ownProject.GINS.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ownProject.GINS.jpa.UserRepository;
import com.ownProject.GINS.user.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		
		User user = userRepository.findByName(name);
		
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return org.springframework.security.core.userdetails.User
							.builder()
							.username(user.getName())
							.password(user.getPassword())
							.roles(user.getRole().name())
							.build();
	}
	
}
