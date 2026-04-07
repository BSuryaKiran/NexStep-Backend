package com.placement.api.dto;

import java.util.Date;

public class NotificationDTO {
    private Long id;
    private String recipientEmail;
    private String subject;
    private String message;
    private String type;
    private String status;
    private String metadata;
    private Date sentAt;
    private Date createdAt;

    public NotificationDTO() {}

    public NotificationDTO(Long id, String recipientEmail, String subject, String message,
                           String type, String status, String metadata, Date sentAt, Date createdAt) {
        this.id = id;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
        this.type = type;
        this.status = status;
        this.metadata = metadata;
        this.sentAt = sentAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
