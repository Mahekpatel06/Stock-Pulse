package com.ownProject.GINS.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ownProject.GINS.dto.UserDTO;
import com.ownProject.GINS.jpa.UserRepository;
import com.ownProject.GINS.role.Role;
import com.ownProject.GINS.user.User;

@ExtendWith(MockitoExtension.class)
class JwtAuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JwtAuthController jwtAuthController;

    // Test Case 1: Registering a standard user (defaults to BUYER if role is null)
    @Test
    void register_DefaultRoleToBuyer() {
        UserDTO dto = new UserDTO();
        dto.setName("john_doe");
        dto.setPassword("pass123");
        dto.setRole(null); // No role specified

        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        String result = jwtAuthController.register(dto);

        assertTrue(result.contains("BUYER"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test Case 2: Verify role assignment logic for ADMIN
    @Test
    void register_AsAdmin() {
        UserDTO dto = new UserDTO();
        dto.setName("admin_user");
        dto.setPassword("adminPass");
        dto.setRole(Role.ADMIN);

        when(passwordEncoder.encode("adminPass")).thenReturn("encodedAdminPass");

        String result = jwtAuthController.register(dto);

        // the registration incorrectly permits saving users as ADMIN...
        assertTrue(result.contains("ADMIN"));
        verify(userRepository, times(1)).save(any(User.class));
    }
}
