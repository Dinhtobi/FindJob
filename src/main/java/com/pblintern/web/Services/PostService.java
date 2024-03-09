package com.pblintern.web.Services;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Responses.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse addPost(PostRequest postRequest);
    PostResponse getPost(int id);

    List<PostResponse> recommendFromInteractive();

    List<PostResponse> recommendFromSkill();
}
