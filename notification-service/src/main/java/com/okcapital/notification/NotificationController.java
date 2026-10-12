package com.okcapital.notification;

import com.okcapital.notification.dto.CreateNotificationRequest;
import com.okcapital.notification.dto.NotificationResponse;
import com.okcapital.notification.service.NotificationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(request));
    }

    @GetMapping
    public List<NotificationResponse> findByRecipient(@RequestParam UUID recipientId) {
        return notificationService.findByRecipient(recipientId);
    }

    @GetMapping("/{notificationId}")
    public NotificationResponse findById(@PathVariable UUID notificationId) {
        return notificationService.findById(notificationId);
    }

    @PatchMapping("/{notificationId}/read")
    public NotificationResponse markRead(@PathVariable UUID notificationId) {
        return notificationService.markRead(notificationId);
    }
}
