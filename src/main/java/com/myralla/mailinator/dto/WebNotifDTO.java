package com.myralla.mailinator.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebNotifDTO {
    private String userId;
    private String email;
    private String content;
    private String message;

}
