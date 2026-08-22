package com.ownProject.GINS.notification;

import com.ownProject.GINS.jpa.NotificationRepository;
import com.ownProject.GINS.exception.customExpClasses.ResourceNotFoundException;
import com.ownProject.GINS.inventory.Inventory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:inventory-system@yourcompany.com}")
    private String mailFrom;

    public NotificationService(NotificationRepository notificRepository, JavaMailSender mailSender) {
        this.notificRepository = notificRepository;
        this.mailSender = mailSender;
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

    @Async // Executed asynchronously on a background thread pool
    public void triggerLowStockAlert(Inventory inv) {
        Notification alert = new Notification();
        alert.setProduct(inv.getProduct());
        alert.setWarehouse(inv.getWareHouse());
        alert.setMessage("LOW STOCK ALERT: " + inv.getProduct().getName() + " in " + inv.getWareHouse().getName()
                + " is down to " + inv.getQuantity());
        alert.setStatus(0); // 0 = UNREAD/SENT

        notificRepository.save(alert);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        
        String recipient = inv.getWareHouse().getContactEmail();
        if (recipient == null || recipient.isEmpty()) {
            recipient = "warehouse-manager@example.com"; // Fallback recipient
        }
        message.setTo(recipient);
        
        message.setSubject("Stock Pulse - Low Stock Warning");
        message.setText("LOW STOCK: " + inv.getProduct().getName() + "\n" 
                + "Alert! Only " + inv.getQuantity() + " items left in " + inv.getWareHouse().getName());

        mailSender.send(message);
    }
}
