package com.okcapital.notification;

import com.okcapital.notification.dto.NotificationResponse;
import com.okcapital.notification.entity.NotificationChannel;
import com.okcapital.notification.entity.NotificationStatus;
import com.okcapital.notification.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    private static final UUID NOTIFICATION_ID = UUID.fromString("e2ac82f7-aab6-4c9d-bb16-1387a9c74d13");
    private static final UUID RECIPIENT_ID = UUID.fromString("a916e98d-2910-4be5-aaf5-0a2d9e5e9240");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void createReturnsCreatedNotification() throws Exception {
        when(notificationService.create(org.mockito.ArgumentMatchers.any())).thenReturn(response(null));

        mockMvc.perform(post("/api/notifications")
                        .with(user("customer"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipientId": "a916e98d-2910-4be5-aaf5-0a2d9e5e9240",
                                  "channel": "IN_APP",
                                  "title": "Application update",
                                  "message": "Your application has been received."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notificationId").value(NOTIFICATION_ID.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.readAt").doesNotExist());
    }

    @Test
    void createRejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/notifications")
                        .with(user("customer"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipientId": "a916e98d-2910-4be5-aaf5-0a2d9e5e9240",
                                  "channel": "IN_APP",
                                  "title": " ",
                                  "message": "Your application has been received."
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listReturnsNotificationsForRecipient() throws Exception {
        when(notificationService.findByRecipient(RECIPIENT_ID)).thenReturn(List.of(response(null)));

        mockMvc.perform(get("/api/notifications")
                        .with(user("customer"))
                        .param("recipientId", RECIPIENT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipientId").value(RECIPIENT_ID.toString()));
    }

    @Test
    void findByIdReturnsNotification() throws Exception {
        when(notificationService.findById(NOTIFICATION_ID)).thenReturn(response(null));

        mockMvc.perform(get("/api/notifications/{notificationId}", NOTIFICATION_ID)
                        .with(user("customer")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").value(NOTIFICATION_ID.toString()));
    }

    @Test
    void findByIdReturnsNotFoundForUnknownNotification() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"))
                .when(notificationService).findById(NOTIFICATION_ID);

        mockMvc.perform(get("/api/notifications/{notificationId}", NOTIFICATION_ID)
                        .with(user("customer")))
                .andExpect(status().isNotFound());
    }

    @Test
    void markReadReturnsReadTimestamp() throws Exception {
        when(notificationService.markRead(NOTIFICATION_ID)).thenReturn(response(Instant.parse("2026-10-11T05:00:00Z")));

        mockMvc.perform(patch("/api/notifications/{notificationId}/read", NOTIFICATION_ID)
                        .with(user("customer"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.readAt").value("2026-10-11T05:00:00Z"));
    }

    private NotificationResponse response(Instant readAt) {
        return new NotificationResponse(
                NOTIFICATION_ID,
                RECIPIENT_ID,
                NotificationChannel.IN_APP,
                "Application update",
                "Your application has been received.",
                NotificationStatus.PENDING,
                Instant.parse("2026-10-11T04:00:00Z"),
                readAt);
    }
}
