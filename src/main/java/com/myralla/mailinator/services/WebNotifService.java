package com.myralla.mailinator.services;

import com.myralla.mailinator.models.Notification;
import com.myralla.mailinator.models.NotificationType;
import com.myralla.mailinator.repositories.NotificationRepository;
import com.myralla.mailinator.repositories.NotificationTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.myralla.mailinator.dto.WebNotifDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WebNotifService {

    @Autowired
    private NotificationRepository notificationRespository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter registerNotification(String userId){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        return emitter;
    }

    public void sendNotification(WebNotifDTO webNotifDTO) {
        log.info("Attempting to send notification to user: {}", webNotifDTO.getUserId());
        SseEmitter emitter = emitters.get(webNotifDTO.getUserId());
        if (emitter == null) {
            log.warn("No active SSE connection found for user: {}", webNotifDTO.getUserId());
            saveNotification(webNotifDTO);
            return;
        }
        try {
            log.info("Sending SSE message: {}", webNotifDTO.getMessage());
            emitter.send(SseEmitter.event().data(webNotifDTO));
            log.info("Message successfully sent!");
        } catch (Exception e) {
            log.error("Error sending SSE message to user {}: {}", webNotifDTO.getUserId(), e.getMessage());
            emitters.remove(webNotifDTO.getUserId());
        }
        saveNotification(webNotifDTO);
    }

    private void saveNotification(WebNotifDTO webNotifDTO) {
        Notification notification = new Notification();
        NotificationType notificationType = notificationTypeRepository.findByType(webNotifDTO.getNotificationType());
        notification.setNotificationType(notificationType);
        notification.setMessage(webNotifDTO.getMessage());
        notification.setSubject(webNotifDTO.getSubject());
        notification.setUserId(webNotifDTO.getUserId());
        notificationRespository.save(notification);
    }

    public ResponseEntity<Object> getNotifications(String keycloakId){
        log.info("Request to get notifications for user : {}", keycloakId);
        List<Notification> notifications = notificationRespository.findTop10ByUserIdOrderByCreatedAtDesc(keycloakId);
        List<WebNotifDTO> response = notifications.stream().map(notification ->
                new WebNotifDTO(
                        notification.getUserId(),
                        notification.getMessage(),
                        notification.getSubject(),
                        notification.getNotificationType().getType()
                )).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
