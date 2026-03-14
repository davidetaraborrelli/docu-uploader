package com.davidetaraborrelli.documentservice.controller;

import com.davidetaraborrelli.documentservice.dto.DocumentContentResponse;
import com.davidetaraborrelli.documentservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/documents")
@RequiredArgsConstructor
public class InternalDocumentController {

    private final DocumentService documentService;

    @GetMapping("/{id}/content")
    public ResponseEntity<DocumentContentResponse> getContent(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getContentForIndexing(id));
    }
}
