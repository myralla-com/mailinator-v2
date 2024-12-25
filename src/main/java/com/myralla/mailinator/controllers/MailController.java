package com.myralla.mailinator.controllers;

import com.myralla.mailinator.dto.EmailDTO;
import com.myralla.mailinator.services.MailgunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/mail")
public class MailController {

    @Autowired
    private MailgunService mailgunService;
    @PostMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> processQueuedMails(@RequestBody EmailDTO emailDTO) {
       return mailgunService.sendEmail(emailDTO);
    }
}
