package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SummaryCandidateForRecruiterResponse {

    private int id;

    private String name;

    private String cvUrl;

    private Boolean isApply;

    private Boolean isInvite;
}
