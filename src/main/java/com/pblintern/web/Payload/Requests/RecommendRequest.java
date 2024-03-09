package com.pblintern.web.Payload.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class RecommendRequest {
    private String jobField;

    private int number;
}
