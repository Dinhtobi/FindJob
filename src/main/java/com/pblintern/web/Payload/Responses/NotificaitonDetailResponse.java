package com.pblintern.web.Payload.Responses;

import com.pblintern.web.Enums.NotificationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificaitonDetailResponse extends NotificationResponse{

    private String message;

    public NotificaitonDetailResponse(int id, Boolean isRead, Date createAt, String postName, String companyLogo, NotificationEnum source, String message) {
        super(id, isRead, createAt, postName, companyLogo, source);
        this.message = message;
    }
}
