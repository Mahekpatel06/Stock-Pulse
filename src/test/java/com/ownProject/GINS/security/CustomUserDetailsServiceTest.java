package com.ownProject.GINS.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ownProject.GINS.jpa.UserRepository;
import com.ownProject.GINS.role.Role;
import com.ownProject.GINS.user.User;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    // Test Case 1: Load user details successfully
    @Test
    void loadUserByUsername_Success() {
        User mockUser = new User();
        mockUser.setName("mahek");
        mockUser.setPassword("hashedPassword");
        mockUser.setRole(Role.SELLER);

        when(userRepository.findByName("mahek")).thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("mahek");

        assertNotNull(userDetails);
        assertEquals("mahek", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SELLER")));
    }

    // Test Case 2: Load user details throws exception when user does not exist
    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByName("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
    }
}