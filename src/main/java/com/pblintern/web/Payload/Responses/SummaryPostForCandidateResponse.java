package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SummaryPostForCandidateResponse {
    private int id;

    private String name;

    private String companyName;

    private String companyLogo;

    private Date createAt;

    private Boolean isSave;

    private Boolean isApply;

    public SummaryPostForCandidateResponse(int id, String name, String companyName, String companyLogo, Date createAt, Boolean isSave, Boolean isApply) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.companyLogo = companyLogo;
        this.createAt = createAt;
        this.isSave = isSave;
        this.isApply = isApply;
    }
}
