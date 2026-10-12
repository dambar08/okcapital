package com.okcapital.notification;

import com.okcapital.notification.dto.CreateNotificationRequest;
import com.okcapital.notification.dto.NotificationResponse;
import com.okcapital.notification.entity.Notification;
import com.okcapital.notification.entity.NotificationChannel;
import com.okcapital.notification.entity.NotificationStatus;
import com.okcapital.notification.repository.NotificationRepository;
import com.okcapital.notification.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private static final UUID RECIPIENT_ID = UUID.fromString("a916e98d-2910-4be5-aaf5-0a2d9e5e9240");
    private static final UUID NOTIFICATION_ID = UUID.fromString("e2ac82f7-aab6-4c9d-bb16-1387a9c74d13");

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createStoresNotificationAsPending() {
        when(notificationRepository.save(org.mockito.ArgumentMatchers.any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        CreateNotificationRequest request = new CreateNotificationRequest(
                RECIPIENT_ID,
                NotificationChannel.EMAIL,
                "Payment received",
                "We received your payment.");

        NotificationResponse response = notificationService.create(request);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notificationCaptor.capture());
        assertEquals(RECIPIENT_ID, notificationCaptor.getValue().getRecipientId());
        assertEquals(NotificationStatus.PENDING, response.status());
        assertNotNull(response.notificationId());
    }

    @Test
    void markReadUpdatesNotificationAndIsIdempotent() {
        Notification notification = new Notification(
                NOTIFICATION_ID,
                RECIPIENT_ID,
                NotificationChannel.IN_APP,
                "Application update",
                "Your application has been received.",
                NotificationStatus.PENDING);
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        NotificationResponse firstResponse = notificationService.markRead(NOTIFICATION_ID);
        NotificationResponse secondResponse = notificationService.markRead(NOTIFICATION_ID);

        assertNotNull(firstResponse.readAt());
        assertEquals(firstResponse.readAt(), secondResponse.readAt());
        assertEquals(NotificationStatus.PENDING, secondResponse.status());
    }

    @Test
    void findByIdReturnsNotFoundWhenNotificationDoesNotExist() {
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> notificationService.findById(NOTIFICATION_ID));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
