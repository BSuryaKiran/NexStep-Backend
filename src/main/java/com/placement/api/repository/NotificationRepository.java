package com.placement.api.repository;

import com.placement.api.entity.Notification;
import com.placement.api.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String email);
    List<Notification> findByStatusOrderByCreatedAtDesc(NotificationStatus status);
    List<Notification> findByRecipientEmailAndStatusOrderByCreatedAtDesc(String email, NotificationStatus status);
}
