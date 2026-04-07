package com.placement.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.api.config.MailgunService;
import com.placement.api.dto.NotificationDTO;
import com.placement.api.dto.NotificationRequest;
import com.placement.api.entity.Notification;
import com.placement.api.entity.NotificationStatus;
import com.placement.api.exception.BadRequestException;
import com.placement.api.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MailgunService mailgunService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationDTO sendEmailNotification(NotificationRequest request) {
        if (request.getRecipientEmail() == null || request.getRecipientEmail().isEmpty()) {
            throw new BadRequestException("Recipient email is required");
        }

        if (request.getSubject() == null || request.getSubject().isEmpty()) {
            throw new BadRequestException("Subject is required");
        }

        if (request.getMessage() == null || request.getMessage().isEmpty()) {
            throw new BadRequestException("Message is required");
        }

        Notification notification = new Notification();
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType() != null ? request.getType() : "general");
        notification.setStatus(NotificationStatus.PENDING);

        // Store metadata as JSON string
        try {
            String metadata = objectMapper.writeValueAsString(request.getMetadata());
            notification.setMetadata(metadata);
        } catch (Exception e) {
            notification.setMetadata("{}");
        }

        notification = notificationRepository.save(notification);

        // Send email asynchronously
        sendEmailAsync(notification);

        return convertToDTO(notification);
    }

    private void sendEmailAsync(Notification notification) {
        new Thread(() -> {
            try {
                String mailgunId = mailgunService.sendEmail(
                        notification.getRecipientEmail(),
                        notification.getSubject(),
                        notification.getMessage()
                );

                notification.setMailgunId(mailgunId);
                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(new java.util.Date());
                notificationRepository.save(notification);
            } catch (Exception e) {
                notification.setStatus(NotificationStatus.FAILED);
                notificationRepository.save(notification);
                System.err.println("Failed to send email: " + e.getMessage());
            }
        }).start();
    }

    public List<NotificationDTO> getNotificationHistory(String email) {
        List<Notification> notifications = notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnsentNotifications() {
        List<Notification> notifications = notificationRepository.findByStatusOrderByCreatedAtDesc(NotificationStatus.PENDING);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new com.placement.api.exception.ResourceNotFoundException("Notification not found"));
        return convertToDTO(notification);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setStatus(notification.getStatus().toString());
        dto.setMetadata(notification.getMetadata());
        dto.setSentAt(notification.getSentAt());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
