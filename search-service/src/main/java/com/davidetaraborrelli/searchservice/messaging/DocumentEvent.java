package com.davidetaraborrelli.searchservice.messaging;

import java.time.Instant;

public record DocumentEvent(
        Long userId,
        Long documentId,
        String documentTitle,
        String eventType,
        Instant timestamp
) {
    public static DocumentEvent indexingCompleted(Long userId, Long documentId, String title) {
        return new DocumentEvent(userId, documentId, title, "INDEXING_COMPLETED", Instant.now());
    }
}
