package com.pblintern.web.Services;

import com.pblintern.web.Entities.Post;
import com.pblintern.web.Payload.DTO.Recommend;
import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.CandidateRecommendRequest;
import com.pblintern.web.Payload.Requests.CandidateRequest;
import com.pblintern.web.Payload.Responses.CandidateForAdminResponse;
import com.pblintern.web.Payload.Responses.CandidateResponse;
import com.pblintern.web.Payload.Responses.SummaryCandidateForRecruiterResponse;

import java.util.List;

public interface CandidateService {
    CandidateResponse updateCandidate(CandidateRequest req, int id);
//    CandidateResponse addCV(CVRequest cvRequest);

    CandidateResponse getCandidate();

    List<CandidateForAdminResponse> getCandidates();

    Recommend RecommendCandidateId(Post post, Boolean all);
    List<SummaryCandidateForRecruiterResponse> recommendCandidateForRecruiter(int id, Boolean all);

}
