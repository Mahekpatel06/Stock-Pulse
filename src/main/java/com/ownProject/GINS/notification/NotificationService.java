package com.ownProject.GINS.notification;

import com.ownProject.GINS.jpa.NotificationRepository;
import com.ownProject.GINS.exception.customExpClasses.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificRepository;

    public NotificationService(NotificationRepository notificRepository) {
        this.notificRepository = notificRepository;
    }

    public List<Notification> getAllNotfic() {
        return notificRepository.findAll();
    }

    public Page<Notification> pgtionNotific(Pageable pageable) {
        return notificRepository.findAll(pageable);
    }

    public Notification markNotificAsRead(Integer id) {
        Notification note = notificRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        note.setStatus(1); // 1 = READ

        return notificRepository.save(note);
    }
}
