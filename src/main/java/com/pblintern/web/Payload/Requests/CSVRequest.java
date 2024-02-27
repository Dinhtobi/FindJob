package com.pblintern.web.Payload.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CSVRequest {
    private int id;
    private String name;
    private int comanyId;
    private String comanyName;
    private String jobField;
    private int salary;
    private String exerience;
    private String level;
    private Date exire;
    private String descrition;
    private String requirements;
}
