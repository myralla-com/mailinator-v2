package com.myralla.mailinator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallbackDTO {
    private String recepient;
    private String status;
    private String message;
}
