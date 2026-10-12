package com.okcapital.notification.repository;

import com.okcapital.notification.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByRecipientIdOrderByCreatedAtDesc(UUID recipientId);
}
