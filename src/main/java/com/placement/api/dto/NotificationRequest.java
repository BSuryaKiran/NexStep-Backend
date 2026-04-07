package com.placement.api.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public class NotificationRequest {
    private String recipientEmail;
    private String subject;
    private String message;
    private String type;
    private Map<String, Object> metadata = new HashMap<>();

    public NotificationRequest() {}

    public NotificationRequest(String recipientEmail, String subject, String message, String type, Map<String, Object> metadata) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
        this.type = type;
        this.metadata = metadata;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        // allows accepting any additional fields in metadata
    }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
