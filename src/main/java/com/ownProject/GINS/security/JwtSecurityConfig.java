	package com.ownProject.GINS.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		return http
					.csrf(customizer -> customizer.disable())
					.authorizeHttpRequests(request -> request
							.requestMatchers("/login", "/register").permitAll()
							.requestMatchers("/inventory/add", 
											 "/inventory/changeQty",
											 "/inventory/transfer/**").hasAnyRole("ADMIN", "SELLER")
							.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/docs").permitAll()
							.anyRequest().authenticated())
					.httpBasic(Customizer.withDefaults())
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			    	.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			    	.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
					.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//		provider.setUserDetailsPasswordService((UserDetailsPasswordService) userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(provider);
		
	}
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		
		var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		
		return jwtAuthenticationConverter;
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails seller = User
//				.withUsername("mahek")
//				.password("{noop}m@123")
//				.roles("SELLER")
//				.build();
//		
//		UserDetails admin = User
//				.withUsername("pray")
//				.password("{noop}p@123")
//				.roles("ADMIN")
//				.build();
//		
//		UserDetails buyer = User
//				.withUsername("gopi")
//				.password("{noop}g@123")
//				.roles("BUYER")
//				.build();
//		
//		return new InMemoryUserDetailsManager(seller, admin, buyer);
//	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public BCryptPasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Value("classpath:certs/public.pem")
	private RSAPublicKey publicKey;
	
	@Value("classpath:certs/private.pem")
	private RSAPrivateKey privateKey;
	
	
//	@Bean
//	public KeyPair keyPair() {
//		try {
//			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//			keyPairGenerator.initialize(2048);
//		
//			return keyPairGenerator.generateKeyPair();
//		} catch(Exception e) {
//			throw new RuntimeException();
//		}
//	}
	
//	@Bean
//	public RSAKey rsaKey(KeyPair keyPair) {
//		return new RSAKey
//				.Builder((RSAPublicKey) keyPair.getPublic())
//				.privateKey(keyPair.getPrivate())
//				.keyID(UUID.randomUUID().toString())
//				.build();
//	}
	
//	@Bean
//	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
//		var jwkSet = new JWKSet(rsaKey);
//		
//		return (jwkSelector, context) -> (jwkSelector.select(jwkSet));
//	}
	
	@Bean
	public JwtDecoder jwtDecoder() { 
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}
	
	@Bean 
	public JwtEncoder jwtEncoder() {

		JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
		
	}
	
}
