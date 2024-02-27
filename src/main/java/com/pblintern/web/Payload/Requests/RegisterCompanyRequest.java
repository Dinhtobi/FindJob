package com.pblintern.web.Payload.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class RegisterCompanyRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String webSite;
    @NotBlank
    private String locations;
    @NotBlank
    private String email;

    private String companyType;

    private String companySize;
    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String taxCode;
    @NotBlank
    private String description;

    private MultipartFile logo;
    @NotNull
    private MultipartFile[] businessLicense;
}
