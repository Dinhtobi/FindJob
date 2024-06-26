package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {

    private int id;
    private String name;
    private String level;
    private String description;
    private String experience;
    private String requirements;
    private Date createAt;
    private Date exprires;
    private int minSalary;

    private int maxSalary;
    private String companyName;
    private String companyLogo;
    private String companyLocation;
    private List<Field> fields;
    private String recruiter;
}
