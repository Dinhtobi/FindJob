package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Entities.WorkExperience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeekerResponse extends RegisterResponse{
    private String address;

    private List<Skills> skills;

    private List<WorkExperience> workExperiences;

    public SeekerResponse(int id, String fullName, String phoneNumber, String email, Long dateOfBirth, boolean gender, String avatarUrl, String address, List<Skills> skills, List<WorkExperience> workExperiences) {
        super(id, fullName, phoneNumber, email, dateOfBirth, gender, avatarUrl);
        this.address = address;
        this.skills = skills;
        this.workExperiences = workExperiences;
    }
}
