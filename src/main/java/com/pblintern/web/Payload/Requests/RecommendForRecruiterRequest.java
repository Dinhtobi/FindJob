package com.pblintern.web.Payload.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendForRecruiterRequest {

    List<CandidateRecommendRequest> candidates ;

    PostRecommendRequest post;

    Boolean useFilter;
}
