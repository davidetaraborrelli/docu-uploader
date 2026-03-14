package com.davidetaraborrelli.documentservice.dto;

import java.time.LocalDateTime;

public record DocumentResponse(
        Long id,
        String title,
        String content,
        Long fileSize,
        String mimeType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
