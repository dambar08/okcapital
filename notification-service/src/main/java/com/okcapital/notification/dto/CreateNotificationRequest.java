package com.okcapital.notification.dto;

import com.okcapital.notification.entity.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateNotificationRequest(
        @NotNull UUID recipientId,
        @NotNull NotificationChannel channel,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 5000) String message) {
}
