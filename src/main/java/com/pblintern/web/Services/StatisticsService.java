package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.StatisticChartForAdminResponse;
import com.pblintern.web.Payload.Responses.StatisticChartResponse;
import com.pblintern.web.Payload.Responses.StatisticsforRecruiterResponse;

public interface StatisticsService {
    StatisticsforRecruiterResponse statisticsForRecruiter();

    StatisticChartResponse statisticsChart(String startDate, String endDate, String type);

    StatisticChartForAdminResponse statisticForAmin(String startDate, String endDate, String type);
}
