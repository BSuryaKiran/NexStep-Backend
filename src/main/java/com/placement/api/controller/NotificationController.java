package com.placement.api.controller;

import com.placement.api.dto.NotificationDTO;
import com.placement.api.dto.NotificationRequest;
import com.placement.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<NotificationDTO> sendEmailNotification(@RequestBody NotificationRequest request) {
        NotificationDTO notification = notificationService.sendEmailNotification(request);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    public ResponseEntity<List<NotificationDTO>> getNotificationHistory() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDTO> notifications = notificationService.getNotificationHistory(email);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<NotificationDTO>> getPendingNotifications() {
        List<NotificationDTO> notifications = notificationService.getUnsentNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
}
