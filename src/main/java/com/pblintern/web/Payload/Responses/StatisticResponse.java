package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticResponse {
    private String name;

    private int numberApply;

    private int numberAccept;

    private int numberWait;

    private int numberRefuse;
}
