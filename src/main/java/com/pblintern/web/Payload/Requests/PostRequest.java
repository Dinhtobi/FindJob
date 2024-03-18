package com.pblintern.web.Payload.Requests;

import com.pblintern.web.Payload.DTO.RequirementPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String level;
    @NotBlank
    private String description;
    @NotBlank
    private List<RequirementPost> requirements;
    @NotBlank
    private String experience;
    @NotNull
    private String exprires;
    @NotBlank
    private String minSalary;
    @NotBlank
    private String maxSalary;
    @NotNull
    private String companyName;
    @NotNull
    private String companyLocation;
    @NotNull
    private String companyLogo;
    @NotNull
    private String field;
}
