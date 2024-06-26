package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.NotificationEnum;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.NotificationRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.NotificaitonDetailResponse;
import com.pblintern.web.Payload.Responses.NotificationResponse;
import com.pblintern.web.Repositories.NotificationRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Repositories.UserRepository;
import com.pblintern.web.Services.NotificationService;
import com.pblintern.web.Utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class INotificationService implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BaseResponse<Boolean> addNotification(NotificationRequest req) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        if(user.getFirstRole().getName().equals(RoleEnum.RECRUITER)){
        req.getReceiveId().stream().forEach(r -> {
            Notification notification = new Notification();
            Post post = postRepository.findById(req.getPostId()).orElseThrow(() -> new NotFoundException("Post not found!"));
            notification.setPost(post);
            User candidate = userRepository.findById(Integer.parseInt(r.getValue())).orElseThrow(() -> new NotFoundException("User received not found!"));
            notification.setUser(candidate);
            notification.setSource(NotificationEnum.JOBAPPLICATIONSTATUS);
            notification.setMessage(req.getMessage());
            notification.setCreateAt(new Date());
            notification.setIsRead(false);
            notificationRepository.save(notification);
        });
        }
        return new BaseResponse<Boolean>(true, "Add notification success");
    }

    @Override
    public List<NotificaitonDetailResponse> getNotifications(int size) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        List<NotificaitonDetailResponse> response = new ArrayList<>();
        if(user.getFirstRole().getName().equals(RoleEnum.CANDIDATE)){
            List<Notification> notifications = notificationRepository.getNotificaitonForCandidate(user.getId());
            notifications.stream().forEach( n -> {
                response.add(new NotificaitonDetailResponse(n.getId(),n.getIsRead(), n.getCreateAt(), n.getPost().getName(), n.getPost().getCompany().getLogo(),n.getSource(),n.getMessage().replaceAll("\\[Tên ứng viên\\]", user.getFullName())));
            });
        }else{
            List<Notification> notifications = notificationRepository.getNotificaitonForRecuiter(user.getId());
            notifications.stream().forEach( n -> {
                response.add(new NotificaitonDetailResponse(n.getId(),n.getIsRead(), n.getCreateAt(), n.getPost().getName(), n.getPost().getCompany().getLogo(),n.getSource(),n.getMessage()));
            });
        }
        return response;
    }

    @Override
    public BaseResponse<String> getModelNotification(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
        String message = Constant.NOTIICATION_ACCEPT.replaceAll("\\[nameReceive\\]", recruiter.getUser().getFullName())
                .replaceAll("\\[postName\\]", post.getName())
                .replaceAll("\\[recruiterName\\]", recruiter.getUser().getFullName())
                .replaceAll("\\[recruiterPosition\\]", recruiter.getPosition())
                .replaceAll("\\[recruiterEmail\\]", recruiter.getUser().getEmail())
                .replaceAll("\\[recruiterPhoneNumber\\]", recruiter.getUser().getPhoneNumber())
                .replaceAll("\\[locationCompany\\]", recruiter.getCompany().getLocation())
                .replaceAll("\\[companyName\\]", recruiter.getCompany().getName())
                .replaceAll("\\[emailCompany\\]", recruiter.getCompany().getEmail())

                ;
        return new BaseResponse<String>(message, "Success");
    }

    @Override
    public BaseResponse<Boolean> updateNotification(int id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotFoundException("Notification not found!"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return new BaseResponse<Boolean>(true,"success");
    }

    @Override
    public BaseResponse<Boolean> inviteApplyNotification(int candidate_id, int post_id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
            Notification notification = new Notification();
            Post post = postRepository.findById(post_id).orElseThrow(() -> new NotFoundException("Post not found!"));
            notification.setPost(post);
            User candidate = userRepository.findById(candidate_id).orElseThrow(() -> new NotFoundException("User received not found!"));
            notification.setUser(candidate);
            notification.setSource(NotificationEnum.OTHER);
            String message = Constant.NOTIFICATION_INVITE_APPLY.replaceAll("\\[candidateName\\]", candidate.getFullName())
                    .replaceAll("\\[postName\\]", post.getName())
                    .replaceAll("\\[recruiterName\\]", recruiter.getUser().getFullName())
                    .replaceAll("\\[recruiterPosition\\]", recruiter.getPosition())
                    .replaceAll("\\[recruiterEmail\\]", recruiter.getUser().getEmail())
                    .replaceAll("\\[recruiterPhoneNumber\\]", recruiter.getUser().getPhoneNumber())
                    .replaceAll("\\[companyLocation\\]", recruiter.getCompany().getLocation())
                    .replaceAll("\\[companyName\\]", recruiter.getCompany().getName())
                    .replaceAll("\\[postId\\]",String.valueOf(post.getId()));
            notification.setMessage(message);
            notification.setCreateAt(new Date());
            notification.setIsRead(false);
            notificationRepository.save(notification);
        return new BaseResponse<Boolean>(true, "Invite candidate success");
    }
}
