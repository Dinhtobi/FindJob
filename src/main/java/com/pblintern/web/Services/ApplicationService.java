package com.pblintern.web.Services;

import com.pblintern.web.Entities.Application;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.SummaryCandidateResponse;

import java.util.List;

public interface ApplicationService {
    BaseResponse<String> addApplication(int id);
    BaseResponse<String> updateViewApplication(int candidateId,int postId);
    BaseResponse<Boolean> updateStatusApplication(int candidateId,int postId,String status);

    BaseResponse<String> deleteApplication(int id);

    Integer getSumApplicationForRecruiter(List<Integer> id);

    List<SummaryCandidateResponse> getApplicationOfPost(int id , String filter);
}
