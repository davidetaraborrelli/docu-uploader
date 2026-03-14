package com.davidetaraborrelli.notificationservice.service;

import com.davidetaraborrelli.notificationservice.dto.NotificationResponse;
import com.davidetaraborrelli.notificationservice.entity.Notification;
import com.davidetaraborrelli.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(Long userId, String type, String message) {
        Notification notification = new Notification(userId, type, message);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notifica non trovata"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getMessage(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
