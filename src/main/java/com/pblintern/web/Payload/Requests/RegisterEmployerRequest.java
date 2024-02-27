package com.pblintern.web.Payload.Requests;

import lombok.Data;

@Data
public class RegisterEmployerRequest extends UserRequest {
    private String position;
}
