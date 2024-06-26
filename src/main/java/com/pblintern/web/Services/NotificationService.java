package com.pblintern.web.Services;

import com.pblintern.web.Payload.Requests.NotificationRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.NotificaitonDetailResponse;
import com.pblintern.web.Payload.Responses.NotificationResponse;

import java.util.List;

public interface NotificationService {
    BaseResponse<Boolean> addNotification(NotificationRequest notificationRequest);

    BaseResponse<Boolean> inviteApplyNotification(int candidate_id, int post_id);

    List<NotificaitonDetailResponse> getNotifications(int size);

    BaseResponse<String> getModelNotification(int postId);

    BaseResponse<Boolean> updateNotification(int id);

}
