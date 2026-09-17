package com.ownProject.GINS.notification;

import com.ownProject.GINS.jpa.NotificationRepository;
import com.ownProject.GINS.exception.customExpClasses.ResourceNotFoundException;
import com.ownProject.GINS.inventory.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificRepository;
    private final BrevoEmailService brevoEmailService;

    public NotificationService(NotificationRepository notificRepository, BrevoEmailService brevoEmailService) {
        this.notificRepository = notificRepository;
        this.brevoEmailService = brevoEmailService;
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
        String productName = (inv.getProduct() != null && inv.getProduct().getName() != null)
                ? inv.getProduct().getName() : "Unknown Product";
        String warehouseName = (inv.getWareHouse() != null && inv.getWareHouse().getName() != null)
                ? inv.getWareHouse().getName() : "Unknown Warehouse";
        Integer quantity = (inv.getQuantity() != null) ? inv.getQuantity() : 0;
        Integer threshold = (inv.getProduct() != null && inv.getProduct().getLow_stock_threshold() != null)
                ? inv.getProduct().getLow_stock_threshold() : 0;

        Notification alert = new Notification();
        alert.setProduct(inv.getProduct());
        alert.setWarehouse(inv.getWareHouse());
        alert.setMessage("LOW STOCK ALERT: " + productName + " in " + warehouseName
                + " is down to " + quantity + " (Threshold: " + threshold + ")");
        alert.setStatus(0); // 0 = UNREAD/SENT

        Notification saved = notificRepository.save(alert);
        log.info("Low stock alert recorded in database with ID: {}", saved.getId());

        String recipient = (inv.getWareHouse() != null) ? inv.getWareHouse().getContactEmail() : null;
        if (recipient == null || recipient.trim().isEmpty()) {
            recipient = "warehouse-manager@example.com"; // Fallback recipient
        }

        String subject = "🚨 Stock Pulse - Low Stock Warning: " + productName;

        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 10px; background-color: #ffffff;\">"
                + "<div style=\"text-align: center; margin-bottom: 20px;\">"
                + "<h1 style=\"color: #dc2626; margin: 0; font-size: 24px;\">⚠️ Low Stock Alert</h1>"
                + "<p style=\"color: #64748b; margin-top: 4px; font-size: 14px;\">Stock Pulse Global Inventory Notification System</p>"
                + "</div>"
                + "<p style=\"color: #334155; font-size: 16px;\">Inventory has dropped to or below the safety threshold. Details are provided below:</p>"
                + "<table style=\"width: 100%; border-collapse: collapse; margin: 20px 0; font-size: 15px;\">"
                + "<tr style=\"background-color: #f8fafc;\"><td style=\"padding: 10px 14px; font-weight: bold; border-bottom: 1px solid #e2e8f0; color: #475569;\">Product</td><td style=\"padding: 10px 14px; border-bottom: 1px solid #e2e8f0; color: #0f172a;\">" + productName + "</td></tr>"
                + "<tr><td style=\"padding: 10px 14px; font-weight: bold; border-bottom: 1px solid #e2e8f0; color: #475569;\">Warehouse</td><td style=\"padding: 10px 14px; border-bottom: 1px solid #e2e8f0; color: #0f172a;\">" + warehouseName + "</td></tr>"
                + "<tr style=\"background-color: #fef2f2;\"><td style=\"padding: 10px 14px; font-weight: bold; border-bottom: 1px solid #e2e8f0; color: #991b1b;\">Current Stock</td><td style=\"padding: 10px 14px; font-weight: bold; border-bottom: 1px solid #e2e8f0; color: #dc2626; font-size: 18px;\">" + quantity + "</td></tr>"
                + "<tr><td style=\"padding: 10px 14px; font-weight: bold; border-bottom: 1px solid #e2e8f0; color: #475569;\">Low Stock Threshold</td><td style=\"padding: 10px 14px; border-bottom: 1px solid #e2e8f0; color: #0f172a;\">" + threshold + "</td></tr>"
                + "</table>"
                + "<p style=\"color: #475569; font-size: 14px;\">Please restock this item or initiate a stock transfer to maintain optimal inventory levels.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #e2e8f0; margin: 24px 0 16px 0;\" />"
                + "<p style=\"color: #94a3b8; font-size: 12px; text-align: center; margin: 0;\">This is an automated notification from Stock Pulse.</p>"
                + "</div>";

        String textContent = "LOW STOCK ALERT: " + productName + " in " + warehouseName + "\n"
                + "Current Stock: " + quantity + "\n"
                + "Safety Threshold: " + threshold + "\n"
                + "Please replenish stock soon to prevent stockouts.";

        brevoEmailService.sendEmail(recipient, warehouseName, subject, htmlContent, textContent);
    }
}
