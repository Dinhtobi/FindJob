package com.pblintern.web.Payload.Requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class CSVRequest {
    private int id;
    private String name;
    private String jobField;
    private String description;
    private String requirements;

    public int getId() {
        return id;
    }

    public String getJobField() {
        return jobField;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJobField(String jobField) {
        this.jobField = jobField;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getDescription() {
        return description != null ? "\"" + description.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;

    }

    public String getRequirements() {
        return requirements != null ? "\"" + requirements.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;

    }

    public String getName() {
        return name != null ? "\"" + name.replaceAll("[\\r\\n]+", " ").replaceAll("\"", "\"\"") + "\"" : null;
    }


}
