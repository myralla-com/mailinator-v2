package com.myralla.mailinator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {
    private String recepient;
    private String subject;
    private String body;
    private String sender;
}
