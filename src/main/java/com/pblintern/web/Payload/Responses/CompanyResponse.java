package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.FieldOfActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    private String name;

    private String webSite;

    private String locations;

    private String companySize;

    private String companyType;

    private String email;

    private String phoneNumber;

    private String description;

    private String logo;

    private String businessLicenseImg;
}
