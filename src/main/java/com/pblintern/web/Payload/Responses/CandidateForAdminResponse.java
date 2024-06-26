package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidateForAdminResponse {

    private int id;

    private String fullName;

    private String email;

    private Boolean gender;

    private Date dob;

    private String phoneNumber;

    private Date createAt;

    private Boolean isNonBlock;
}
