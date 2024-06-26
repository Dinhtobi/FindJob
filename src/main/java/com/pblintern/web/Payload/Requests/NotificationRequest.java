package com.pblintern.web.Payload.Requests;

import com.pblintern.web.Payload.DTO.SelectedDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationRequest {

    private List<SelectedDTO> receiveId;

    private int postId;

    private String message;
}
