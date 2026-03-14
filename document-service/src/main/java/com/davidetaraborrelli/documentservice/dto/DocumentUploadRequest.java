package com.davidetaraborrelli.documentservice.dto;

import jakarta.validation.constraints.NotBlank;

public record DocumentUploadRequest(
        @NotBlank String title,
        @NotBlank String content
) {}
