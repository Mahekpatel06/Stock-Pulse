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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification APIs")
public class NotificationController {

	private final NotificationService notificService;
	private final BrevoEmailService brevoEmailService;

	public NotificationController(NotificationService notificService, BrevoEmailService brevoEmailService) {
		this.notificService = notificService;
		this.brevoEmailService = brevoEmailService;
	}
	
	@GetMapping
	@Operation(summary = "get all notifications")
	public List<Notification> getAllNotifications() {
		return notificService.getAllNotfic();
	}
	
	@GetMapping("/pagination")
	@Operation(summary = "get notifications in diff pages acc. to your choice")
	public Page<Notification> pgNotifi(Pageable pageable) {
		return notificService.pgtionNotific(pageable);
	}
	
	@PutMapping("/{id}/read")
	@Operation(summary = "do mark as read to notification")
	public ResponseEntity<Notification> markAsRead(@PathVariable Integer id) {
		Notification updatedNotification = notificService.markNotificAsRead(id);
		return ResponseEntity.ok(updatedNotification);
	}

	@GetMapping("/test-email")
	@Operation(summary = "test Brevo email integration with live diagnostic report")
	public ResponseEntity<Map<String, Object>> testEmail(@RequestParam(defaultValue = "taps2109@gmail.com") String to) {
		Map<String, Object> result = brevoEmailService.sendEmailWithDiagnostics(
				to,
				"Warehouse Manager",
				"🧪 Stock Pulse - Live Test Alert",
				"<div style='font-family:Arial,sans-serif;padding:20px;border:1px solid #ddd;border-radius:8px;'>"
						+ "<h2 style='color:#16a34a;'>✅ Stock Pulse Email Connection Verified!</h2>"
						+ "<p>This is a test notification confirming that your Brevo API integration and Render environment variables are working properly.</p>"
						+ "<p><strong>Recipient:</strong> " + to + "</p>"
						+ "</div>",
				"Stock Pulse Email Connection Verified! Your Brevo API integration is working properly."
		);
		return ResponseEntity.ok(result);
	}
}
