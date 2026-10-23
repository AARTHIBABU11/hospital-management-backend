package com.hospital.service;

import com.hospital.entity.Notification;
import com.hospital.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsForUser(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    public List<Notification> getUnreadForUser(String email) {
        return notificationRepository.findByRecipientEmailAndIsReadFalseOrderByCreatedAtDesc(email);
    }

    public void sendNotification(String recipientEmail, String title, String message, String type) {
        Notification notification = new Notification(recipientEmail, title, message, type);
        notificationRepository.save(notification);
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(String email) {
        List<Notification> unread = notificationRepository.findByRecipientEmailAndIsReadFalseOrderByCreatedAtDesc(email);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}
