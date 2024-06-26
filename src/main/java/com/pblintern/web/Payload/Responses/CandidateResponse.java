package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.Field;
import com.pblintern.web.Entities.Skills;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponse extends RegisterResponse{
    private String address;

    private List<Skills> skills;

    private String cvUrl;

    private Field field;

    public CandidateResponse(int id, String fullName, String phoneNumber, String email, Long dateOfBirth, boolean gender, String avatarUrl, String address, List<Skills> skills, String cvUrl, Field field) {
        super(id, fullName, phoneNumber, email, dateOfBirth, gender, avatarUrl);
        this.address = address;
        this.skills = skills;
        this.cvUrl = cvUrl;
        this.field = field;

    }
}
