package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends LoginResponse {

    private String fullName;

    private String phoneNumber;

    private String email;

    private String avatar;

    private boolean gender;

    private Date dateOfBirth;

    public UserResponse(String status, RoleEnum role, int id, String fullName, String phoneNumber, String email, String avatar, boolean gender, Date dateOfBirth) {
        super(status, role, id);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
