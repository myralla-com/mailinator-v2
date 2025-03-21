package com.myralla.mailinator.services;

import com.myralla.mailinator.dto.CallbackDTO;
import com.myralla.mailinator.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class EmailBatchConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailgunService mailgunService;

    @Async
    public void processEmailBatch(EmailDTO message){
        log.info("Processing email batch: {}", message.getRecepient());
//       mailgun api sending logic here ... wip

//       TODO: add error handling and retry logic
//        mailgunService.sendEmail(message);

        CallbackDTO callbackMessage = new CallbackDTO();
        callbackMessage.setRecepient(message.getRecepient());
        callbackMessage.setStatus("sent");
        callbackMessage.setMessage("Email sent successfully");

        rabbitTemplate.convertAndSend("email.callback.queue", callbackMessage);
        log.info("Callback message sent for : {}", message.getRecepient());
    }
}
