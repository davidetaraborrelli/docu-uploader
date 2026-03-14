package com.davidetaraborrelli.documentservice.controller;

import com.davidetaraborrelli.common.dto.UserInfo;
import com.davidetaraborrelli.documentservice.dto.DocumentResponse;
import com.davidetaraborrelli.documentservice.dto.DocumentUploadRequest;
import com.davidetaraborrelli.documentservice.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponse> upload(
            @Valid @RequestBody DocumentUploadRequest request,
            Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        DocumentResponse response = documentService.upload(user.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> list(Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        return ResponseEntity.ok(documentService.listByUser(user.id()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        return ResponseEntity.ok(documentService.getByIdAndUser(id, user.id()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        documentService.delete(id, user.id());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
