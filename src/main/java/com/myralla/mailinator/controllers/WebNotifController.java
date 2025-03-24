package com.myralla.mailinator.controllers;

import com.myralla.mailinator.services.WebNotifService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/webNotif")
public class WebNotifController {

    private static final Logger log = LoggerFactory.getLogger(WebNotifController.class);
    @Autowired
    private WebNotifService webNotifService;

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String userId){
        log.info("Request to subscribe to notifications for user : {}", userId);
        return webNotifService.registerNotification(userId);
    }

    @GetMapping(value = "/notifications/{keycloakId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNotifications(@PathVariable String keycloakId){
        log.info("Request to get notifications for user : {}", keycloakId);
        return webNotifService.getNotifications(keycloakId);
    }


}
