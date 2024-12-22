package com.myralla.mailinator.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/mail")
public class MailController {

    @PostMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public void processQueuedMails() {

    }
}
