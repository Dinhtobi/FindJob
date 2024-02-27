package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Responses.PostResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IPostService implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private EmployeerRepository employeerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PostResponse addPost(PostRequest req) {
        Post post = new Post();
        post.setName(req.getName());
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());
        post.setRequirements(req.getRequirements());
        post.setSalary(Integer.parseInt(req.getSalary()));
        post.setCreateAt(new Date());
        if(req.getExprires() != null){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(req.getExprires());
                post.setExpire(date);
            }catch(Exception e){
                post.setExpire(null);
            }
        }else
            post.setExpire(null);
        post.setExperience(req.getExperience());
        Company company = companyRepository.findById(req.getIdCompany()).orElseThrow(() -> new NotFoundException("Company not found!"));
        post.setCompany(company);

        FieldOfActivity fieldOfActivity = fieldRepository.findById(req.getIdField()).orElseThrow(() -> new NotFoundException("Field not found!"));
        post.setFieldOfActivity(fieldOfActivity);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        Employer employer = employeerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Employer not found!"));
        post.setEmployer(employer);

        postRepository.save(post);

        return new PostResponse(post.getName(),post.getLevel(),post.getDescription(),post.getExperience(),post.getRequirements(),post.getCreateAt().getTime(),post.getExpire().getTime(),post.getSalary(),company.getName(),fieldOfActivity.getName(),user.getFullName());
    }

    @Override
    public PostResponse getPost(int id) {
        Post post =  postRepository.findById((id)).orElseThrow(() -> new NotFoundException("Post not found!"));
        User user = userRepository.findById(post.getEmployer().getId()).orElseThrow(() -> new NotFoundException("User not found"));
        return new PostResponse(post.getName(),post.getLevel(),post.getDescription(),post.getExperience(),post.getRequirements(),post.getCreateAt().getTime(),post.getExpire().getTime(),post.getSalary(),post.getCompany().getName(),post.getFieldOfActivity().getName(),user.getFullName());
    }
}
