package com.davidetaraborrelli.documentservice.dto;

public record DocumentContentResponse(
        Long documentId,
        String title,
        String content,
        Long userId
) {}
