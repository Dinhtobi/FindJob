package com.pblintern.web.Payload.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class RecruiterRequest extends UserRequest {
    private String position;

    @NotNull
    private Boolean isHadCompany;
    private int companyId;
    private String companyName;
    private String companyWebSite;
    private String companyLocation;
    private String companyEmail;
    private String companyType;
    private String companySize;
    private String companyDescription;
    private String companyLogo;

}
