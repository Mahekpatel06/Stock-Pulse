package com.ownProject.GINS.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.jpa.NotificationRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification APIs")
public class NotificationController {

	private NotificationRepository notificationRepo;

	public NotificationController(NotificationRepository notificationRepo) {
		super();
		this.notificationRepo = notificationRepo;
	}
	
	
	@GetMapping
	@Operation(summary = "get all notifications")
	public List<Notification> getAllNotifications() {
		return notificationRepo.findAll();
	}
	
	@GetMapping("/pagination")
	@Operation(summary = "get notifications in diff pages acc. to your choice")
	public Page<Notification> pgNotifi(Pageable pageable) {
		return notificationRepo.findAll(pageable);
	}
	
	@PutMapping("/{id}/read")
	@Operation(summary = "do mark as read to notification")
	public ResponseEntity<Notification> markAsRead(@PathVariable Integer id) {
		
		Notification note = notificationRepo.findById(id)
				.orElseThrow( () -> new RuntimeException("Notification not Found"));
		
		note.setStatus(1);		// 1 = READ
		
		return ResponseEntity.ok(notificationRepo.save(note));
	}
}
