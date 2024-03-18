package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.InteractiveResponse;

import java.util.List;

public interface HistoryInteractiveService {
    BaseResponse<String> addInteractive(int id);

    List<InteractiveResponse> getInteractive();
}
