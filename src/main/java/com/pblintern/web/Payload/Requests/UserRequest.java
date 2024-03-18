package com.pblintern.web.Payload.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNumber;
    boolean gender;
    @NotBlank
    private String dateOfBirth;
    @NotNull
    private MultipartFile avatar;
}
