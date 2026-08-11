package com.ownProject.GINS.notification;

import com.ownProject.GINS.jpa.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private NotificationRepository notificRepository;


    public List<Notification> getAllNotfic() {
        return notificRepository.findAll();
    }

    public Page<Notification> pgtionNotific(Pageable pageable) {
        return notificRepository.findAll(pageable);
    }

    public ResponseEntity<Notification> markNotificAsRead(Integer id) {
        Notification note = notificRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Notification not Found"));

        note.setStatus(1);		// 1 = READ

        return ResponseEntity.ok(notificRepository.save(note));
    }
}
