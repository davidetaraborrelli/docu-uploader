package com.davidetaraborrelli.notificationservice.service;

import com.davidetaraborrelli.notificationservice.dto.NotificationResponse;
import com.davidetaraborrelli.notificationservice.entity.Notification;
import com.davidetaraborrelli.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createNotification_success() {
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        notificationService.createNotification(1L, "UPLOAD_COMPLETED", "Documento caricato");

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void getByUser_returnsNotifications() {
        Notification n = new Notification(1L, "UPLOAD_COMPLETED", "Documento caricato");
        n.setId(1L);
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(n));

        List<NotificationResponse> result = notificationService.getByUser(1L);

        assertEquals(1, result.size());
        assertEquals("UPLOAD_COMPLETED", result.get(0).type());
    }

    @Test
    void markAsRead_success() {
        Notification n = new Notification(1L, "UPLOAD_COMPLETED", "Documento caricato");
        n.setId(1L);
        when(notificationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(n));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        notificationService.markAsRead(1L, 1L);

        assertTrue(n.isRead());
        verify(notificationRepository).save(n);
    }

    @Test
    void markAsRead_notFound_throws() {
        when(notificationRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> notificationService.markAsRead(99L, 1L));
    }
}
