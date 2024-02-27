package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEmployeerResponse extends RegisterResponse{

    private String position;

    public RegisterEmployeerResponse(int id, String fullName, String phoneNumber, String email, Long dateOfBirth, boolean gender, String avatarUrl, String position) {
        super(id, fullName, phoneNumber, email, dateOfBirth, gender, avatarUrl);
        this.position = position;
    }
}
