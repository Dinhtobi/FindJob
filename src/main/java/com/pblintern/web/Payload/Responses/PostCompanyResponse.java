package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Entities.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCompanyResponse {
    private int id;
    private String name;
    private Date createAt;
    private Date exprires;
    private String experience;
    private int minSalary;
    private int maxSalary;
    private String companyName;
    private String companyLogo;
}
