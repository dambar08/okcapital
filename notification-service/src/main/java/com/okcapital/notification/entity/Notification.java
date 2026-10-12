package com.okcapital.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "notification_id")
    private UUID notificationId;

    @Column(nullable = false)
    private UUID recipientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 5000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    private Instant readAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Notification() {
    }

    public Notification(
            UUID notificationId,
            UUID recipientId,
            NotificationChannel channel,
            String title,
            String message,
            NotificationStatus status) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.channel = channel;
        this.title = title;
        this.message = message;
        this.status = status;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void updateTimestamp() {
        updatedAt = Instant.now();
    }

    public void markRead() {
        if (readAt == null) {
            readAt = Instant.now();
        }
    }
}
