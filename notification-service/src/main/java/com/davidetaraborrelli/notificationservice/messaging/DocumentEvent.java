package com.davidetaraborrelli.notificationservice.messaging;

import java.time.Instant;

public record DocumentEvent(
        Long userId,
        Long documentId,
        String documentTitle,
        String eventType,
        Instant timestamp
) {}
