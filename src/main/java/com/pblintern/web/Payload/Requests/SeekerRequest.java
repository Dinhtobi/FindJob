package com.pblintern.web.Payload.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeekerRequest extends UserRequest {
    private String address;
    private List<SkillRequest> skills;
    private List<WorkExperienceRequest> workExperiences;
}
