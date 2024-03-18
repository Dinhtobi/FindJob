package com.pblintern.web.Services;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.PagedResponse;
import com.pblintern.web.Payload.Responses.PostDetailResponse;
import com.pblintern.web.Payload.Responses.PostResponse;

import java.util.Date;
import java.util.List;

public interface PostService {
    PostResponse addPost(PostRequest postRequest);
    PostResponse updatePost(PostRequest postRequest,int id);
    PostDetailResponse getPost(int id);
    List<PostResponse> getMyJob();
    List<PostResponse> recommendFromInteractive();
    List<PostResponse> recommendFromSkill();
    PagedResponse<PostResponse> getJob(Date from, Date to, String searchField, String keyword, String sortType, String sortField, String location,
             String postingDate,String expericence,int size, int page);
    BaseResponse<String> deleteJob(int id);

    List<PostResponse> recommend();
}
