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
    private String salary;
    private String exerience;
    private String level;
    private Date exire;
    private String description;
    private String requirements;


    public String getName() {
        return name != null ? "\"" + name.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;
    }

    public String getDescrition() {
        return description != null ? "\"" + description.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;
    }

    public String getRequirements() {
        return requirements != null ? "\"" + requirements.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;
    }
}
