package com.ownProject.GINS.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.jpa.UserRepository;
import com.ownProject.GINS.user.User;

@RestController
public class JwtAuthController {
	
	private JwtTokenService jwtTokenService;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	
	public JwtAuthController(JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, UserRepository userRepository) {
		super();
		this.jwtTokenService = jwtTokenService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
	}

	@PostMapping("/register")
	public String register(@RequestBody User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(user.getRole() == null || user.getRole() == User.Role.ADMIN) {
			user.setRole(User.Role.BUYER);
		}
		
		userRepository.save(user);
		
		return "User registered successfully as " + user.getRole();
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody User request) {
		
		var authentication = authenticationManager.authenticate
				(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
		
		var token = jwtTokenService.generateToken(authentication);
				
		return ResponseEntity.ok(new JwtTokenResponse(token));
	}
}

record JwtRegisterResponse(String name, String password) { }
record JwtTokenResponse(String token) { }