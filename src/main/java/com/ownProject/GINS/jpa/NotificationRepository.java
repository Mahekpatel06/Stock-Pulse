package com.ownProject.GINS.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
}
