package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

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
    private String salary;
    private String companyName;
    private String companyLogo;
    private String companyLocation;
    private String field;
    private String employeer;
}
