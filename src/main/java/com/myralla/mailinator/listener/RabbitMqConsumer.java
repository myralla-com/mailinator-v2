package com.myralla.mailinator.listener;

import com.myralla.mailinator.services.EmailBatchConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.myralla.mailinator.dto.EmailDTO;

@Slf4j
@Component
public class RabbitMqConsumer {

    @Autowired
    private EmailBatchConsumer emailBatchConsumer;

    @RabbitListener(queues = "email.batch.queue")
    public void consumeMessage(EmailDTO message) {
        log.info("Received a Queue for : {}", message.getRecepient());
        emailBatchConsumer.processEmailBatch(message);
    }

}
