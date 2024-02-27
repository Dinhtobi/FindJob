package com.pblintern.web.Payload.Requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperienceRequest {
    private String name;
    private String timeStart;
    private String timeEnd;
    private String position;
    private String description;
}
