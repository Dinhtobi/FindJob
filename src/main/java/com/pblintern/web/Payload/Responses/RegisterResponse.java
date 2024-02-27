package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private int id;

    private String fullName;

    private String phoneNumber;

    private String email;

    private Long dateOfBirth;

    private boolean gender;

    private String avatarUrl;
}
