package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.BaseResponse;

public interface ApplicationService {
    BaseResponse<String> addApplication(int id);

    BaseResponse<String> deleteApplication(int id);
}
