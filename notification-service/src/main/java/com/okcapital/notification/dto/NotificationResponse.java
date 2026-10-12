package com.okcapital.notification.dto;

import com.okcapital.notification.entity.Notification;
import com.okcapital.notification.entity.NotificationChannel;
import com.okcapital.notification.entity.NotificationStatus;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID notificationId,
        UUID recipientId,
        NotificationChannel channel,
        String title,
        String message,
        NotificationStatus status,
        Instant createdAt,
        Instant readAt) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getRecipientId(),
                notification.getChannel(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getReadAt());
    }
}
