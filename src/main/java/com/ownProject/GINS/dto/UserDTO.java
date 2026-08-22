package com.ownProject.GINS.dto;

import com.ownProject.GINS.role.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

	@NotBlank(message = "Username is required")
	public String name;

	@NotBlank(message = "Password is required")
	public String password;
	
	@Enumerated(EnumType.STRING)
	public Role role;
	
	public UserDTO() {
		super();
	}

	public UserDTO(String name, String password, Role role) {
		super();
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDTO [name=" + name + ", password=" + password + ", role=" + role + "]";
	}
	
}
