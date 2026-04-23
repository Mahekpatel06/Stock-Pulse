package com.ownProject.GINS.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.dto.UserDTO;
import com.ownProject.GINS.jpa.UserRepository;
import com.ownProject.GINS.role.Role;
import com.ownProject.GINS.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Jwt Auth APIs")
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
	@Operation(summary = "First Do Register into Stock Pulse")
	public String register(@RequestBody UserDTO userDTO) {
	    
	    // 1. Create a new Entity object
	    User user = new User();
	    
	    Role role = userDTO.getRole();

	    user.setName(userDTO.getName());
	    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    
	    if(role == null || role.equals("ADMIN")) {
	        user.setRole(user.role.BUYER); 
	    } else {
	        user.setRole(role);
	    }
	    
	    userRepository.save(user);
	    
	    return "User registered successfully as " + user.getRole();
	}
	
	
	@PostMapping("/login")
	@Operation(summary = "login into Stock Pulse to explore")
	public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody UserDTO request) {
		
		var authentication = authenticationManager.authenticate
				(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
		
		var token = jwtTokenService.generateToken(authentication);
				
		return ResponseEntity.ok(new JwtTokenResponse(token));
	}
}

record JwtRegisterResponse(String name, String password) { }
record JwtTokenResponse(String token) { }
