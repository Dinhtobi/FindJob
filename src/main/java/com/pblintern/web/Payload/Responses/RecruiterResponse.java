package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterResponse extends RegisterResponse{

    private String position;

    private int companyId;

    public RecruiterResponse(int id, String fullName, String phoneNumber, String email, Long dateOfBirth, boolean gender, String avatarUrl, String position,int companyId) {
        super(id, fullName, phoneNumber, email, dateOfBirth, gender, avatarUrl);
        this.position = position;
        this.companyId = companyId;
    }
}
