package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostResponse {

    private String name;
    private String level;
    private String description;
    private String expericence;
    private String requirements;
    private Long createAt;
    private Long exprires;
    private int salary;
    private String company;
    private String field;
    private String employeer;
}
