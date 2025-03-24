package com.myralla.mailinator.listener;

import com.myralla.mailinator.dto.WebNotifDTO;
import com.myralla.mailinator.services.MailgunService;
import com.myralla.mailinator.services.WebNotifService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.myralla.mailinator.dto.EmailDTO;

@Slf4j
@Component
public class RabbitMqConsumer {

    @Autowired
    private WebNotifService webNotifService;

    @Autowired
    private MailgunService mailgunService;

    @RabbitListener(queues = "email.batch.queue")
    public void consumeEmailMessage(EmailDTO email) {
        mailgunService.sendEmail(email);
    }

    @RabbitListener(queues = "web.notification.queue")
    public void consumeWebNotification(WebNotifDTO notification) {
        webNotifService.sendNotification(notification);
    }




}
