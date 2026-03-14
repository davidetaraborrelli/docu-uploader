package com.davidetaraborrelli.documentservice.messaging;

import java.time.Instant;

public record DocumentEvent(
        Long userId,
        Long documentId,
        String documentTitle,
        String eventType,
        Instant timestamp
) {
    public static DocumentEvent uploadCompleted(Long userId, Long documentId, String title) {
        return new DocumentEvent(userId, documentId, title, "UPLOAD_COMPLETED", Instant.now());
    }
}
