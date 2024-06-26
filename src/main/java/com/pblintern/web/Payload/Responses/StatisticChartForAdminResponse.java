package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticChartForAdminResponse {
    private List<StatisticForAdminResponse> statistic;

    private int sumPost;

    private double avgPost;

    private int sumApply;

    private double avgApply;

    private int sumPostHN;

    private int sumPostHCM;

    private int sumPostDN;

    private int sumOther;
}
