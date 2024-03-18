package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class PostDetailResponse extends PostResponse {
    private String minSalary;

    private String maxSalary;

    public PostDetailResponse(int id, String name, String level, String description, String experience, String requirements, Date createAt, Date exprires, String salary, String companyName, String companyLogo, String companyLocation, String field, String employeer, String minSalary, String maxSalary) {
        super(id, name, level, description, experience, requirements, createAt, exprires, salary, companyName, companyLogo, companyLocation, field, employeer);
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
}
