package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.NotificationEnum;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.DTO.Recommend;
import com.pblintern.web.Payload.Requests.*;
import com.pblintern.web.Payload.Responses.CandidateForAdminResponse;
import com.pblintern.web.Payload.Responses.CandidateResponse;
import com.pblintern.web.Payload.Responses.SummaryCandidateForRecruiterResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.CandidateService;
import com.pblintern.web.Services.StorageService;
import com.pblintern.web.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ICandidateService implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StorageService storageService;

    @Value("${AI.recommendUrl}")
    private String recommendUrl ;

    @Override
    public CandidateResponse updateCandidate(CandidateRequest req, int id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        User user = userService.updateUserInfo(new UserRequest(req.getFullName(), req.getPhoneNumber(), req.isGender(), req.getDateOfBirth(), req.getAvatar()), id);
        if (req.getSkills() == null) {
            candidate.setSkills(null);
        } else {
            Set<Skills> set = new HashSet<>();
            List<SkillRequest> skills = new ArrayList<>();
            req.getSkills().stream().forEach(r -> {
                skills.add(new SkillRequest(r.getValue()));
            });
            List<SkillRequest> skills_unavailable = skills.stream().filter(s -> !skillRepository.findByName(s.getName()).isPresent()).collect(Collectors.toList());
            skills_unavailable.stream().forEach(s -> {
                Skills skill = new Skills();
                skill.setName(s.getName().toLowerCase());
                skillRepository.save(skill);
                set.add(skill);
            });
            skills.removeAll(skills_unavailable);
            skills.stream().forEach(s -> {
                Skills skill = skillRepository.findByName(s.getName().toLowerCase()).get();
                set.add(skill);
            });
            candidate.setSkills(set);
        }
        if (req.getAddress() != null)
            candidate.setAddress(req.getAddress());
        if (candidate.getField().getId() != req.getFieldId()){
            Field field = fieldRepository.findById(req.getFieldId()).orElseThrow(() -> new NotFoundException("Field not found!"));
            candidate.setField(field);
        }
        candidateRepository.save(candidate);
        String avatarUrl = storageService.createPresignedGetUrl("avatarfindjob", user.getAvatar()).getUrl();
        String cvUrl = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).getUrl();

        return new CandidateResponse(candidate.getId(), user.getFullName(), user.getPhoneNumber(), user.getEmail(), user.getDateOfBirth().getTime(), user.isGender(), avatarUrl, candidate.getAddress(), candidate.getSkills() == null ? null : candidate.getSkills().stream().collect(Collectors.toList()),cvUrl,candidate.getField());

    }


    @Override
    public CandidateResponse getCandidate() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Candidate not found!"));
        Candidate candidate = candidateRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Candidate not found!"));

        String avatarUrl = storageService.createPresignedGetUrl("avatarfindjob", user.getAvatar()).getUrl();
        String cvUrl = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).getUrl();
        return new CandidateResponse(candidate.getId(), user.getFullName(), user.getPhoneNumber(), user.getEmail(), user.getDateOfBirth().getTime(), user.isGender(), avatarUrl, candidate.getAddress(), candidate.getSkills() == null ? null : candidate.getSkills().stream().collect(Collectors.toList()),cvUrl,candidate.getField());
    }


    @Override
    public Recommend RecommendCandidateId(Post post, Boolean all) {
        String postField = post.getFields().stream().map(Field::getName).collect(Collectors.joining(", "));
        List<CandidateRecommendRequest> candidates = new ArrayList<>();
        if(!all){
            List<Application> applications = applicationRepository.findByPostId(post.getId());
            if(!applications.isEmpty()){
                applications.stream().forEach(a -> {
                    Candidate candidate = candidateRepository.findById(a.getCandidate_id()).orElseThrow(() -> new NotFoundException("Candidate not found!"));
                    String skill = candidate.getSkills().stream().map(Skills::getName).collect(Collectors.joining(", "));
                    candidates.add(new CandidateRecommendRequest(candidate.getId(), skill, candidate.getField().getName()));
                });
            }
        }else{
            List<Candidate> candidateAll = candidateRepository.findAll();
            candidateAll.stream().forEach(c -> {
                String skill = c.getSkills().stream().map(Skills::getName).collect(Collectors.joining(", "));
                candidates.add(new CandidateRecommendRequest(c.getId(), skill, c.getField().getName()));
            });
        }
        RecommendForRecruiterRequest recommendRequest = new RecommendForRecruiterRequest(candidates, new PostRecommendRequest(post.getId(),postField, post.getRequirement() ), all ? true: false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(converter);

        Recommend recommendCandidate = restTemplate.postForObject(recommendUrl+"candidate", recommendRequest, Recommend.class);
        return recommendCandidate;
    }

    @Override
    public List<SummaryCandidateForRecruiterResponse> recommendCandidateForRecruiter(int id, Boolean all) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
        Recommend recommendCandidate = RecommendCandidateId(post, all);
        List<SummaryCandidateForRecruiterResponse> response = new ArrayList<>();
        if(!all) {
            recommendCandidate.getIds().stream().forEach(i -> {
                Candidate candidate = candidateRepository.findById(i).orElseThrow(() -> new NotFoundException("Candidate not found!"));
                String cv = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).toString();
                response.add(new SummaryCandidateForRecruiterResponse(candidate.getId(), candidate.getUser().getFullName(), cv, true, true));
            });
        }else{
            recommendCandidate.getIds().stream().forEach(i -> {
                Candidate candidate = candidateRepository.findById(i).orElseThrow(() -> new NotFoundException("Candidate not found!"));
                String cv = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).toString();
                Optional<Application> application = applicationRepository.findByCandidateIdAndPostId(candidate.getId(), post.getId());
                Optional<Notification> notification = notificationRepository.getNotificationByPostIdAndCandidateId(post.getId(), candidate.getId());
                Boolean isInvite = true;
                if(notification.isEmpty())
                    isInvite = false;
                response.add(new SummaryCandidateForRecruiterResponse(candidate.getId(), candidate.getUser().getFullName(), cv, application.isEmpty() ?  false : true, isInvite));
            });
        }
        return response;
    }

    @Override
    public List<CandidateForAdminResponse> getCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<CandidateForAdminResponse> response = new ArrayList<>();
        candidates.stream().forEach(c -> {
            User user = c.getUser() ;
            response.add(new CandidateForAdminResponse(c.getId(), user.getFullName(), user.getEmail(), user.isGender(), user.getDateOfBirth(), user.getPhoneNumber(), user.getCreateAt(),user.isNonBlock()));
        });
        return response;
    }
}
