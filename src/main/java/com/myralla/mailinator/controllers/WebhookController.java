package com.myralla.mailinator.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping(path = "/mailgun")
    public void processWebhook(@RequestBody Map<String, Object> data) {
        log.info("Received webhook data: {}", data);
    }
}
