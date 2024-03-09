package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.DTO.RecommendPost;
import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Requests.RecommendRequest;
import com.pblintern.web.Payload.Requests.SkillRequest;
import com.pblintern.web.Payload.Responses.PostResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import com.pblintern.web.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private HistoryInteractiveRepository historyInteractiveRepository;

    @Value("${AI.recommendUrl}")
    private String recommendUrl ;

    @Override
    public PostResponse addPost(PostRequest req) {
        Post post = new Post();
        post.setName(req.getName());
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());
        post.setRequirements(req.getRequirements());
        post.setSalary(req.getSalary());
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

    @Override
    public List<PostResponse> recommendFromInteractive() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<HistoryInteractiveProjection> historyInteractiveProjections = historyInteractiveRepository.findFiveDESC(email);
        RestTemplate restTemplate = new RestTemplate();
        Map<Integer, Integer> idsPost= new HashMap<>();
        historyInteractiveProjections.stream().forEach( h -> {
            if(!idsPost.containsKey(h.getIdPost())){
                idsPost.put(h.getIdPost(),1);
            }else{
                idsPost.put(h.getIdPost(), idsPost.get(h.getIdPost()) + 1);
            }
        });

        Set<Integer> set = idsPost.keySet();
        List<RecommendRequest> listRequest = new ArrayList<>();
        for(Integer key : set){
            Post post = postRepository.findById(key).orElseThrow(() -> new NotFoundException("Post not found"));
            listRequest.add(new RecommendRequest(post.getFieldOfActivity().getName(),idsPost.get(key)));
        }

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(converter);

        RecommendPost recommendPost = restTemplate.postForObject(recommendUrl+"field", listRequest , RecommendPost.class);

        List<PostResponse> posts = new ArrayList<>();
        recommendPost.getIds().stream().forEach(id -> {
                Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
                User user = userRepository.findById(post.getEmployer().getId()).orElseThrow(() -> new NotFoundException("Employer not found!"));
                PostResponse postResponse = new PostResponse(post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                        post.getCreateAt() == null ? null : post.getCreateAt().getTime(),post.getExpire() == null ? null : post.getExpire().getTime(), post.getSalary(),post.getCompany().getName(),post.getFieldOfActivity().getName(),user.getFullName());
                posts.add(postResponse);
        });
        return posts;
    }

    @Override
    public List<PostResponse> recommendFromSkill() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seeker seeker = seekerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        SkillRequest skillRequest =new SkillRequest("");
        seeker.getSkills().stream().forEach(s -> {
            skillRequest.setName(skillRequest.getName().concat(" " + s.getName()));
        });
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(converter);

        RecommendPost recommendPost = restTemplate.postForObject(recommendUrl+"skill", skillRequest , RecommendPost.class);

        List<PostResponse> posts = new ArrayList<>();
        recommendPost.getIds().stream().forEach(id -> {
            Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
            User user = userRepository.findById(post.getEmployer().getId()).orElseThrow(() -> new NotFoundException("Employer not found!"));
            PostResponse postResponse = new PostResponse(post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt().getTime(),post.getExpire() == null ? null : post.getExpire().getTime(), post.getSalary(),post.getCompany().getName(),post.getFieldOfActivity().getName(),user.getFullName());
            posts.add(postResponse);
        });
        return posts;
    }
}
