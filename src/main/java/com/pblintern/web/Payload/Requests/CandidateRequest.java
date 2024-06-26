package com.pblintern.web.Payload.Requests;

import com.pblintern.web.Payload.DTO.RequirementPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest extends UserRequest {
    private String address;

    private List<RequirementPost> skills;

    private int fieldId ;

}
