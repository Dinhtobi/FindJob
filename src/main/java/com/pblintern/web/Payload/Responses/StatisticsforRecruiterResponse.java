package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsforRecruiterResponse {
    private int sumPost;

    private int sumCv;

    private int sumShowPost;

    private int sumCvRecommend;
}
