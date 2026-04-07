package com.placement.api.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailgunService {

    @Value("${mailgun.api-key:}")
    private String apiKey;

    @Value("${mailgun.domain:}")
    private String domain;

    @Value("${mailgun.from:noreply@nexstep.com}")
    private String fromEmail;

    private static final String MAILGUN_API_URL = "https://api.mailgun.net/v3";

    public String sendEmail(String recipientEmail, String subject, String body) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || domain == null || domain.isEmpty()) {
            // If Mailgun is not configured, return a mock ID
            System.out.println("Mailgun not configured. Mock sending email to: " + recipientEmail);
            return "mock_" + System.currentTimeMillis();
        }

        String url = MAILGUN_API_URL + "/" + domain + "/messages";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        // Add authentication
        String auth = "api:" + apiKey;
        String authEncoded = Base64.getEncoder().encodeToString(auth.getBytes());
        conn.setRequestProperty("Authorization", "Basic " + authEncoded);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String data = "from=" + fromEmail +
                "&to=" + recipientEmail +
                "&subject=" + java.net.URLEncoder.encode(subject, "UTF-8") +
                "&text=" + java.net.URLEncoder.encode(body, "UTF-8") +
                "&html=" + java.net.URLEncoder.encode(convertToHtml(body), "UTF-8");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(data.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            // Parse response to get the message ID
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            return root.get("id").asText();
        } else {
            throw new Exception("Failed to send email via Mailgun. Status: " + responseCode);
        }
    }

    private String convertToHtml(String text) {
        return "<p>" + text.replace("\n", "<br>") + "</p>";
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && domain != null && !domain.isEmpty();
    }
}
