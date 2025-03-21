package com.myralla.mailinator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
@Slf4j
public class WebhookService {

    public void processWebhook(Map<String, Object> data) {
        log.info("Received webhook data: {}", data);
    }

}
