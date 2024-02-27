package com.pblintern.web.Payload.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String level;
    @NotBlank
    private String description;
    @NotBlank
    private String requirements;
    @NotBlank
    private String experience;
    @NotNull
    private String exprires;
    @NotBlank
    private String salary;
    @NotNull
    private int idCompany;
    @NotNull
    private int idField;
}
