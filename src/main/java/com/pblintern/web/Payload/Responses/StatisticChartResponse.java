package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticChartResponse {
    private List<StatisticResponse> statistic;

    private int sumApply;

    private int sumAccept;

    private int sumWait;

    private int sumRefuse;
}
