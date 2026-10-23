package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.entity.Notification;
import com.hospital.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthenticated"));
        List<Notification> list = notificationService.getNotificationsForUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Notifications fetched", list));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthenticated"));
        List<Notification> list = notificationService.getUnreadForUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Unread notifications fetched", list));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.ok("Marked as read"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        if (authentication != null) {
            notificationService.markAllAsRead(authentication.getName());
        }
        return ResponseEntity.ok(ApiResponse.ok("All marked as read"));
    }
}
