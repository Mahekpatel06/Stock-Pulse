package com.ownProject.GINS.notification;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class BrevoEmailServiceTest {

    @Test
    void testBrevoEmailServiceCreation() {
        BrevoEmailService service = new BrevoEmailService();
        assertNotNull(service);

        // Test with empty API key
        ReflectionTestUtils.setField(service, "apiKey", "");
        boolean result = service.sendEmail("test@example.com", "Test User", "Test Subject", "<p>Hello</p>", "Hello");
        assertFalse(result, "Should return false when API key is empty");
    }

    @Test
    void testBrevoUriResolution() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.brevo.com/v3");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.brevo.com/v3/smtp/email"))
                .andRespond(withSuccess("{\"messageId\":\"123\"}", MediaType.APPLICATION_JSON));

        RestClient client = builder.build();
        String res = client.post().uri("/smtp/email").retrieve().body(String.class);
        assertEquals("{\"messageId\":\"123\"}", res);
    }
}
