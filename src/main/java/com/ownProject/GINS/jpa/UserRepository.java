package com.ownProject.GINS.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByName(String name);
	
}
