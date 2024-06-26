package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Enums.NotificationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationResponse {

    private int id;

    private Boolean isRead;

    private Date createAt;

    private String postName;

    private String companyLogo;

    private NotificationEnum source;
}
