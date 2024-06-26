package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.ApplicationEnum;
import com.pblintern.web.Enums.NotificationEnum;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.DTO.Recommend;
import com.pblintern.web.Payload.DTO.SelectedDTO;
import com.pblintern.web.Payload.Requests.NotificationRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.SummaryCandidateResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.ApplicationService;
import com.pblintern.web.Services.CandidateService;
import com.pblintern.web.Services.NotificationService;
import com.pblintern.web.Services.StorageService;
import com.pblintern.web.Utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IApplicationService implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CandidateService candidateService;

    @Override
    public BaseResponse<String> addApplication(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        Optional<Application> applicationOptional = applicationRepository.findByCandidateIdAndPostId(candidate.getId(), post.getId());
        if(applicationOptional.isEmpty()){
        Application application = new Application();
        application.setCandidate_id(candidate.getId());
        application.setPost_id(post.getId());
        application.setCreateAt(new Date());
        application.setIsRead(false);
        application.setStatus(ApplicationEnum.WAITING);
        applicationRepository.save(application);
            return new BaseResponse<>(null, "Success!");
        }else{
            return new BaseResponse<>(null, "exits!");
        }
    }

    @Override
    public BaseResponse<String> updateViewApplication(int candidateId,int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));

        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        Application application = applicationRepository.findByCandidateIdAndPostId(candidate.getId(), post.getId()).orElseThrow(() -> new NotFoundException("Application not found!"));
        application.setIsRead(true);
        applicationRepository.save(application);
        return new BaseResponse<>(null, "Success!");
    }

    @Override
    public BaseResponse<Boolean> updateStatusApplication(int candidateId,int postId,String status) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));


        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new NotFoundException("Candidate not found!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
        Application application = applicationRepository.findByCandidateIdAndPostId(candidate.getId(), post.getId()).orElseThrow(() -> new NotFoundException("Application not found!"));
        application.setIsRead(true);
        if(status.equals(ApplicationEnum.ACCEPT.toString())){
            application.setStatus(ApplicationEnum.ACCEPT);
        }else{
            application.setStatus(ApplicationEnum.REFUSE);
            String message = Constant.NOTIFICATION_REFUSE.replaceAll("\\[companyName\\]", recruiter.getCompany().getName())
                    .replaceAll("\\[candidateName\\]",candidate.getUser().getFullName())
                    .replaceAll("\\[postName\\]", post.getName())
                    .replaceAll("\\[recruiterName\\]", recruiter.getUser().getFullName())
                    .replaceAll("\\[recruiterPosition\\]", recruiter.getPosition())
                    .replaceAll("\\[recruiterEmail\\]", recruiter.getUser().getEmail())
                    .replaceAll("\\[recruiterPhoneNumber\\]", recruiter.getUser().getPhoneNumber()) ;
            Notification notification = new Notification();
            notification.setPost(post);
            notification.setUser(candidate.getUser());
            notification.setSource(NotificationEnum.JOBAPPLICATIONSTATUS);
            notification.setMessage(message);
            notification.setIsRead(false);
            notification.setCreateAt(new Date());
            notificationRepository.save(notification);
            }
        applicationRepository.save(application);
        return new BaseResponse<Boolean>(true, "Success!");
    }

    @Override
    public BaseResponse<String> deleteApplication(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        Application application = applicationRepository.findByCandidateIdAndPostId(candidate.getId(), post.getId()).orElseThrow(() -> new NotFoundException("Application not found!"));
        applicationRepository.delete(application);
        return new BaseResponse<>(null, "Success!");
    }

    @Override
    public Integer getSumApplicationForRecruiter(List<Integer> ids) {
        return applicationRepository.getSumApplicationForRecruiter(ids).getSum();
    }

    @Override
    public List<SummaryCandidateResponse> getApplicationOfPost(int id , String filter) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
        Recommend recommendCandidate = candidateService.RecommendCandidateId(post, false);
        List<SummaryCandidateResponse> response = new ArrayList<>();
        recommendCandidate.getIds().stream().forEach(a -> {
            Application application = applicationRepository.findByCandidateIdAndPostId(a,id).orElseThrow(() -> new NotFoundException("Applicaiton not found!"));
            Candidate candidate = candidateRepository.findById(a).orElseThrow(()-> new NotFoundException("Candidate not found!"));
            String cvUrl = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).getUrl();
            response.add(new SummaryCandidateResponse(candidate.getId(), candidate.getUser().getFullName(), application.getCreateAt(), cvUrl, application.getIsRead(), application.getStatus().toString()));
        });

        if(filter.equals(Constant.FILTER_SEARCH_CANDIDATE_ACCEPT)){
            return response.stream().filter(p -> p.getStatus().equals(Constant.FILTER_SEARCH_CANDIDATE_ACCEPT)).collect(Collectors.toList());
        }else if(filter.equals(Constant.FILTER_SEARCH_CANDIDATE_REFUSE)){
            return response.stream().filter(p -> p.getStatus().equals(Constant.FILTER_SEARCH_CANDIDATE_REFUSE)).collect(Collectors.toList());
        } else if(filter.equals(Constant.FILTER_SEARCH_CANDIDATE_READED)){
            return response.stream().filter(p -> p.getIsRead()).collect(Collectors.toList());
        } else if (filter.equals(Constant.FILTER_SEARCH_CANDIDATE_NOTREAD)){
            return response.stream().filter(p -> !p.getIsRead()).collect(Collectors.toList());
        }else {
            return response.stream().sorted(Comparator.comparing(SummaryCandidateResponse::getIsRead)).collect(Collectors.toList());
        }

    }
}
