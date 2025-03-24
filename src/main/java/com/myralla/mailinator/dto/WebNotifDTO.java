package com.myralla.mailinator.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WebNotifDTO {
    private String userId;
    private String message;
    private String subject;
    private String notificationType;
}
