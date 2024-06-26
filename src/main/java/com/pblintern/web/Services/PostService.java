package com.pblintern.web.Services;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Responses.*;

import java.util.Date;
import java.util.List;

public interface PostService {
    PostResponse addPost(PostRequest postRequest);
    PostResponse updatePost(PostRequest postRequest,int id);
    PostDetailResponse getPost(int id);
    List<SummaryPostForCandidateResponse> getMyApplicationForCandidate();
    List<SummaryPostForRecruiterResponse> getMyJobForRecruiter(Boolean isShow);

    List<SummaryPostForCandidateResponse> getMyFavouriteForCandidate();

    List<PostResponse> recommendFromInteractive();
    List<SummaryPostForCandidateResponse> recommendPostForCandiddate();



    PagedResponse<PostResponse> getJob(Date from, Date to, String searchField, String keyword, String sortType, String sortField, String location,
             String postingDate,String expericence,int salary,int field,int size, int page);
    BaseResponse<String> deleteJob(int id);

    List<PostResponse> recommend();


}
