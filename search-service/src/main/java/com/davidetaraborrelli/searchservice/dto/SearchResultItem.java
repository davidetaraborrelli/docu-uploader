package com.davidetaraborrelli.searchservice.dto;

public record SearchResultItem(
        Long documentId,
        String title,
        float score,
        Long userId
) {}
