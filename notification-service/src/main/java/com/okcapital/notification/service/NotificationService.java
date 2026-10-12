package com.okcapital.notification.service;

import com.okcapital.notification.dto.CreateNotificationRequest;
import com.okcapital.notification.dto.NotificationResponse;
import com.okcapital.notification.entity.Notification;
import com.okcapital.notification.entity.NotificationStatus;
import com.okcapital.notification.repository.NotificationRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse create(CreateNotificationRequest request) {
        Notification notification = new Notification(
                UUID.randomUUID(),
                request.recipientId(),
                request.channel(),
                request.title(),
                request.message(),
                NotificationStatus.PENDING);
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    public List<NotificationResponse> findByRecipient(UUID recipientId) {
        return notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(recipientId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public NotificationResponse findById(UUID notificationId) {
        return NotificationResponse.from(getNotification(notificationId));
    }

    public NotificationResponse markRead(UUID notificationId) {
        Notification notification = getNotification(notificationId);
        notification.markRead();
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    private Notification getNotification(UUID notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }
}
