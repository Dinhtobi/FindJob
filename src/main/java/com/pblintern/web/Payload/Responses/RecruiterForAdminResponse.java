package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecruiterForAdminResponse {
    private int id;

    private String fullName;

    private String email;

    private String position;

    private Boolean isNonBlock;
}
