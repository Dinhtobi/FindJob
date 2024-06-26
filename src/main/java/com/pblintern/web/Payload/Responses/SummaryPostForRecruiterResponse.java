package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SummaryPostForRecruiterResponse {
    private int id;

    private String name;

    private Date createAt;

    private Date expire;

    private int minSalary;

    private int maxSalary;

}
