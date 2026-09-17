package com.ownProject.GINS.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailService.class);

    private final RestClient restClient;

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.sender.email:inventory@stockpulse.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Stock Pulse}")
    private String senderName;

    public BrevoEmailService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .build();
    }

    /**
     * Sends an email using Brevo's Transactional Email REST API over HTTPS port 443.
     * This avoids blocked SMTP ports on cloud hosts like Render.
     *
     * @param toEmail      Recipient email address
     * @param toName       Recipient name (e.g. Warehouse Name)
     * @param subject      Subject line
     * @param htmlContent  Rich HTML body
     * @param textContent  Plain text fallback
     * @return true if successfully sent, false otherwise
     */
    public boolean sendEmail(String toEmail, String toName, String subject, String htmlContent, String textContent) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("Brevo API key is not configured! (Set 'BREVO_API_KEY' environment variable). Email to {} was not sent.", toEmail);
            return false;
        }

        try {
            Map<String, Object> payload = new HashMap<>();

            Map<String, String> sender = new HashMap<>();
            sender.put("name", (senderName != null && !senderName.isBlank()) ? senderName : "Stock Pulse");
            sender.put("email", (senderEmail != null && !senderEmail.isBlank()) ? senderEmail : "inventory@stockpulse.com");
            payload.put("sender", sender);

            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", toEmail);
            if (toName != null && !toName.isBlank()) {
                recipient.put("name", toName);
            }
            List<Map<String, String>> toList = new ArrayList<>();
            toList.add(recipient);
            payload.put("to", toList);

            payload.put("subject", subject);

            if (htmlContent != null && !htmlContent.isBlank()) {
                payload.put("htmlContent", htmlContent);
            }
            if (textContent != null && !textContent.isBlank()) {
                payload.put("textContent", textContent);
            }

            log.info("Sending email via Brevo REST API to {} from {}", toEmail, sender.get("email"));

            String responseBody = restClient.post()
                    .uri("/smtp/email")
                    .header("api-key", apiKey.trim())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String errorBody = new String(response.getBody().readAllBytes());
                        log.error("Brevo API error response [{}]: {}", response.getStatusCode(), errorBody);
                        throw new RuntimeException("Brevo API returned error " + response.getStatusCode() + ": " + errorBody);
                    })
                    .body(String.class);

            log.info("Email successfully sent via Brevo to {}. Response: {}", toEmail, responseBody);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email to {} via Brevo API: {}", toEmail, e.getMessage());
            return false;
        }
    }
}
