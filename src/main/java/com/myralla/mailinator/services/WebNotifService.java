package com.myralla.mailinator.services;

import com.myralla.mailinator.models.Notification;
import com.myralla.mailinator.models.NotificationType;
import com.myralla.mailinator.repositories.NotificationRespository;
import com.myralla.mailinator.repositories.NotificationTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myralla.mailinator.dto.WebNotifDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebNotifService {

    @Autowired
    private NotificationRespository notificationRespository;

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

    public void sendNotification(WebNotifDTO webNotifDTO){
        log.info("Attempting to send notification to user: {}", webNotifDTO.getUserId());
        SseEmitter emitter = emitters.get(webNotifDTO.getUserId());
        if (emitter == null) {
            log.warn("No active SSE connection found for user: {}", webNotifDTO.getUserId());
            return;
        }
        try {
            log.info("Sending SSE message: {}", webNotifDTO.getMessage());
            emitter.send(SseEmitter.event().data(webNotifDTO));
            log.info("Message successfully sent!");

            // save notification to database
            Notification notification = new Notification();
            NotificationType notificationType = notificationTypeRepository.findByType(webNotifDTO.getNotificationType());
            notification.setNotificationType(notificationType);
            notification.setMessage(webNotifDTO.getMessage());
            notification.setUserId(webNotifDTO.getUserId());
            notificationRespository.save(notification);

        } catch (Exception e) {
            log.error("Error sending SSE message to user {}: {}", webNotifDTO.getUserId(), e.getMessage());
            emitters.remove(webNotifDTO.getUserId());
        }
    }

}
