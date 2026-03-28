package com.ownProject.GINS.notification;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.jpa.NotificationRepository;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private NotificationRepository notificationRepo;

	public NotificationController(NotificationRepository notificationRepo) {
		super();
		this.notificationRepo = notificationRepo;
	}
	
	
	@GetMapping
	public List<Notification> getAllNotifications() {
		return notificationRepo.findAll();
	}
	
	@PutMapping("/{id}/read")
	public ResponseEntity<Notification> markAsRead(@PathVariable Integer id) {
		
		Notification note = notificationRepo.findById(id)
				.orElseThrow( () -> new RuntimeException("Notification not Found"));
		
		note.setStatus(1);		// 1 = READ
		
		return ResponseEntity.ok(notificationRepo.save(note));
	}
}
