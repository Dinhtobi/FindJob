package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String status;

    private RoleEnum role;

    private int id;
}
