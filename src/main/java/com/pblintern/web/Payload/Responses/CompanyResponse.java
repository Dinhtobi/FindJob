package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    private String name;

    private String webSite;

    private String locations;

    private String companySize;

    private String companyType;

    private String description;

    private String logo;

}
