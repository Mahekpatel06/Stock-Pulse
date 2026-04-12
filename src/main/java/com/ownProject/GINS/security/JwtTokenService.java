package com.ownProject.GINS.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

	private JwtEncoder jwtEncoder;
	
	public JwtTokenService(JwtEncoder jwtEncoder) {
		super();
		this.jwtEncoder = jwtEncoder;
	}

	public String generateToken(Authentication authentication) {
		
		var roles = authentication.getAuthorities()
							.stream()
							.map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining(" "));
		
		var claims = JwtClaimsSet.builder()
							.issuer("self")
							.issuedAt(Instant.now())
							.expiresAt(Instant.now().plusSeconds(60 * 60))
							.subject(authentication.getName())
							.claim("roles", roles)
							.build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims))
					.getTokenValue();
	}
	
}
