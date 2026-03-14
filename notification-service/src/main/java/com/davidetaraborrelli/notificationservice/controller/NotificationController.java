package com.davidetaraborrelli.notificationservice.controller;

import com.davidetaraborrelli.common.dto.UserInfo;
import com.davidetaraborrelli.notificationservice.dto.NotificationResponse;
import com.davidetaraborrelli.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> list(Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getByUser(user.id()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {
        UserInfo user = (UserInfo) authentication.getPrincipal();
        notificationService.markAsRead(id, user.id());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }
}
