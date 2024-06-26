package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SummaryCandidateResponse {
    private int id;

    private String name;

    private Date timeApply;

    private String cvUrl;

    private Boolean isRead;

    private String status;
}
