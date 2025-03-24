package com.myralla.mailinator.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebNotifDTO {
    private String userId;
    private String message;
    private String notificationType;
}
