package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.RecruiterForAdminResponse;

import java.util.List;

public interface RecruiterService {
    BaseResponse<Boolean> activeAccount(String code, int id);

    List<RecruiterForAdminResponse> getRecruiters();
}
