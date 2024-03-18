package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Application;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Seeker;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Repositories.ApplicationRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.SeekerRepository;
import com.pblintern.web.Services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IApplicationService implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Override
    public BaseResponse<String> addApplication(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seeker seeker = seekerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        Optional<Application> applicationOptional = applicationRepository.findBySeekerIdAndPostId(seeker.getId(), post.getId());
        if(applicationOptional.isEmpty()){
        Application application = new Application();
        application.setSeeker(seeker);
        application.setJobPost(post);
        applicationRepository.save(application);
            return new BaseResponse<>(null, "Success!");
        }else{
            return new BaseResponse<>(null, "exits!");
        }
    }

    @Override
    public BaseResponse<String> deleteApplication(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seeker seeker = seekerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        Application application = applicationRepository.findBySeekerIdAndPostId(seeker.getId(), post.getId()).orElseThrow(() -> new NotFoundException("Application not found!"));
        applicationRepository.delete(application);
        return new BaseResponse<>(null, "Success!");
    }
}
