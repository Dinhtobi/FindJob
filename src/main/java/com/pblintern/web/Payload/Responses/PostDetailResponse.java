package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class PostDetailResponse extends PostResponse {

    private Boolean isFavourite;

    public PostDetailResponse(int id, String name, String level, String description, String experience, String requirements, Date createAt, Date exprires, int minSalary, int maxSalary, String companyName, String companyLogo, String companyLocation, List<Field> fields, String recruiter, Boolean isFavourite) {
        super(id, name, level, description, experience, requirements, createAt, exprires, minSalary, maxSalary, companyName, companyLogo, companyLocation, fields, recruiter);
        this.isFavourite = isFavourite;
    }
}
