package com.davidetaraborrelli.notificationservice.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String type,
        String message,
        boolean read,
        LocalDateTime createdAt
) {}
