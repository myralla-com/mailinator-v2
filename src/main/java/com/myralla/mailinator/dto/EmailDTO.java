package com.myralla.mailinator.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailDTO {
//    TODO: match the fields of the required payload in the mailgun API
    private String recepient;
    private String subject;
    private String body;
    private String sender;

    public String printEmailDTO(){
      return "EmailDTO: " + this.recepient + " " + this.subject + " " + this.body + " " + this.sender;
    }
}
