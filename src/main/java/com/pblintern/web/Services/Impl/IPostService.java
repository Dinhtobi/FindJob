package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.DTO.Recommend;
import com.pblintern.web.Payload.Requests.*;
import com.pblintern.web.Payload.Responses.*;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import com.pblintern.web.Services.PostService;
import com.pblintern.web.Services.StorageService;
import com.pblintern.web.Utils.AppUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IPostService implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private HistoryInteractiveRepository historyInteractiveRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private StorageService storageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${AI.recommendUrl}")
    private String recommendUrl ;

    @Override
    public PostResponse addPost(PostRequest req) {
        Post post = new Post();
        post.setName(req.getName());

        post.setRequirement(req.getRequirements());
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());



        post.setMinSalary(req.getMinSalary());
        post.setMaxSalary(req.getMaxSalary());
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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        Recruiter recruiter = recruiterRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Employer not found!"));

        post.setCompany(recruiter.getCompany());

        Set<Field> set = new HashSet<>();

        req.getField().stream().forEach(f -> {
            Field field = fieldRepository.findByName(f.getValue().toString()).orElseThrow(() -> new NotFoundException("Field not found!"));
            set.add(field);
        });
        post.setFields(set);

        post.setRecruiter(recruiter);
        postRepository.save(post);
        return new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirement().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getMinSalary(), post.getMaxSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFields() == null ? null : post.getFields().stream().collect(Collectors.toList()),user.getFullName());
    }

    @Override
    public PostDetailResponse getPost(int id) {
        Post post =  postRepository.findById((id)).orElseThrow(() -> new NotFoundException("Post not found!"));
        User user = userRepository.findById(post.getRecruiter().getId()).orElseThrow(() -> new NotFoundException("User not found"));

        Boolean isFavourite = false;
        Optional<Favourite> favouriteOptional = favouriteRepository.findByCandidateAndPost(user.getId(), post.getId());
        if(favouriteOptional.isPresent()){
            isFavourite = true;
        }

        return new PostDetailResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirement().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getMinSalary(), post.getMaxSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFields() == null ? null : post.getFields().stream().collect(Collectors.toList()),user.getFullName(),isFavourite);
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
            listRequest.add(new RecommendRequest(post.getFields() == null ? null : post.getFields().stream().map( p -> p.getName()).collect(Collectors.toList()), idsPost.get(key)));
        }

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(converter);

        Recommend recommendPost = restTemplate.postForObject(recommendUrl+"field", listRequest , Recommend.class);

        List<PostResponse> posts = new ArrayList<>();
        recommendPost.getIds().stream().forEach(id -> {
                Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
                User user = userRepository.findById(post.getRecruiter().getId()).orElseThrow(() -> new NotFoundException("Employer not found!"));
            PostResponse postResponse = new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirement().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getMinSalary(), post.getMaxSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFields() == null ? null : post.getFields().stream().collect(Collectors.toList()),user.getFullName());
            posts.add(postResponse);
        });
        return posts;
    }

    @Override
    public List<SummaryPostForCandidateResponse> recommendPostForCandiddate() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        String skill = candidate.getSkills().stream().map(Skills::getName).collect(Collectors.joining(", "));
        RecommendForCandidateRequest recommendCandidateRequest =new RecommendForCandidateRequest(skill,candidate.getField().getName(),10);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(converter);

        Recommend recommendPost = restTemplate.postForObject(recommendUrl+"skill", recommendCandidateRequest , Recommend.class);

        List<SummaryPostForCandidateResponse> posts = new ArrayList<>();
        recommendPost.getIds().stream().forEach(id -> {
            Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found!"));
            User user = userRepository.findById(candidate.getId()).orElseThrow(() -> new NotFoundException("Candidate not found!"));
            Optional<Application> application = applicationRepository.findByCandidateIdAndPostId(user.getId(), post.getId());
            Boolean isApply = true;
            if(application.isEmpty()){
                isApply = false;
            }
            Optional<Favourite> favouriteOptional = favouriteRepository.findByCandidateAndPost(user.getId(), post.getId());
            Boolean isSave = true;
            if(favouriteOptional.isEmpty()){
                isSave = false;
            }
            posts.add(new SummaryPostForCandidateResponse(post.getId(),post.getName(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCreateAt(),isSave,isApply)
            );
        });
        return posts;
    }




    @Override
    public List<PostResponse> recommend() {
        List<PostResponse> responses = recommendFromInteractive();
//        List<PostResponse> recommend_unavailable = recommendFrromSkill().stream().filter(r -> !responses.contains(r)).collect(Collectors.toList());
//        responses.addAll(recommend_unavailable);
        return responses;
    }

    @Override
    public PagedResponse<PostResponse> getJob(Date from, Date to, String searchField, String keyword, String sortType, String sortField, String location,
                                              String postingDate,String experience,int salary,int field,int size, int page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Post> root = criteriaQuery.from(Post.class);

        Join<Post, Recruiter> recruiterJoin = root.join("recruiter", JoinType.INNER);
        Join<Post,Company> companyJoin = root.join("company", JoinType.INNER);
        Join<Recruiter, User> userJoin = recruiterJoin.join("user", JoinType.INNER);
        criteriaQuery.multiselect(
                userJoin.alias("user"),
                recruiterJoin.alias("recruiter"),
                root.alias("post"),
                companyJoin.alias("company")
        );



        List<Predicate> predicates = new ArrayList<>();
        if(from != null && to!= null){
            Path<Date> fieldCreateDate = root.get("createAt");
            Predicate predicateFrom = criteriaBuilder.greaterThanOrEqualTo(fieldCreateDate, from);
            Predicate predicateTo = criteriaBuilder.lessThanOrEqualTo(fieldCreateDate, to);
            predicates.add(predicateFrom);
            predicates.add(predicateTo);
        }

        if(sortField == null)
            sortField = "id";
        if(sortType == null)
            sortType = "asc";
        Path<Object> sortRoute = null;
        try{
            if(sortField.equals("fullName")){
                sortRoute = userJoin.get(sortField);
            }else{
                sortRoute = root.get(sortField);
            }
        }catch(IllegalArgumentException e){
            throw new BadRequestException("Invalid sortField " + sortField);
        }
        Order order = "asc".equalsIgnoreCase(sortType) ? criteriaBuilder.asc(sortRoute) : criteriaBuilder.desc(sortRoute);
        criteriaQuery.orderBy(order);
        if(keyword != null){
            if(searchField.equals("name")){
                Path<String> fieldName = root.get("name");
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(fieldName), "%" + keyword.toLowerCase() + "%");
                predicates.add(predicate);
            }else{
                throw new BadRequestException("Invalid field " + searchField);
            }
        }
        if(location != null ){
            Path<String> fieldLocation = companyJoin.get("location");
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(fieldLocation), "%" + location.toLowerCase() + "%");
            predicates.add(predicate);
        }

        if(salary != 0 ){
            Path<Integer> fieldSalary = root.get("maxSalary");
           Predicate predicate =criteriaBuilder.lessThan(fieldSalary, salary);
           predicates.add(predicate);
        }

        if(field != 0 ){
            Join<Post, Field> fieldJoin = root.join("fields", JoinType.INNER);
            criteriaQuery.multiselect(
                    userJoin.alias("user"),
                    recruiterJoin.alias("recruiter"),
                    root.alias("post"),
                    companyJoin.alias("company"),
                    fieldJoin.alias("field")
            );
            Path<String> fieldField = fieldJoin.get("id");
            Predicate predicate = criteriaBuilder.equal(fieldField, field);
            predicates.add(predicate);
        }
        if(postingDate != null){
            try {
                Path<Date> fieldDate = root.get("createAt");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(postingDate);
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(fieldDate, date);
                predicates.add(predicate);
            } catch(Exception e){

            }
        }
        if(experience != null ){
            Path<String> fieldExperience= root.get("experience");
            Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(fieldExperience), experience.toLowerCase());
            predicates.add(predicate);
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
        AppUtils.validatePageNumberAndSize(page, size);

        int totalResults = typedQuery.getResultList().size();

        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);
        List<PostResponse> postResponses = new ArrayList<>();
        for(Tuple tuple : typedQuery.getResultList()){
            User user = tuple.get("user", User.class);
            Post post = tuple.get("post", Post.class);
            PostResponse postResponse = new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirement().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getMinSalary(), post.getMaxSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFields() == null ? null : post.getFields().stream().collect(Collectors.toList()),user.getFullName());
            postResponses.add(postResponse);

        }
        List<PostResponse> distinctElements = postResponses.stream()
                .distinct()
                .collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<PostResponse> pageResponse = new PageImpl<PostResponse>(postResponses, pageRequest, totalResults);
        return new PagedResponse<PostResponse>(pageResponse.getContent(), pageResponse.getNumber(), pageResponse.getSize(),
                pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.isLast());
    }

    @Override
    public List<SummaryPostForCandidateResponse> getMyApplicationForCandidate() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        List<SummaryPostForCandidateResponse> reponses = new ArrayList<>();
        if(user.getFirstRole().getName().toString().equals(RoleEnum.CANDIDATE.toString())){
            List<Application> apps = applicationRepository.findByCandidateId(user.getId());
            apps.stream().forEach( a -> {
                Post post = postRepository.findById(a.getPost_id()).orElseThrow(() -> new NotFoundException("Post not found!"));
                Optional<Favourite> favouriteOptional = favouriteRepository.findByCandidateAndPost(user.getId(), post.getId());
                Boolean isSave = true;
                if(favouriteOptional.isEmpty()){
                    isSave = false;
                }
                reponses.add(new SummaryPostForCandidateResponse(post.getId(),post.getName(),post.getCompany()== null ? null : post.getCompany().getName(),
                        post.getCompany()== null ? null : post.getCompany().getLogo(),a.getCreateAt(),isSave,true)
                        );
            });
        }
        return reponses;
    }

    @Override
    public List<SummaryPostForCandidateResponse> getMyFavouriteForCandidate() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        List<SummaryPostForCandidateResponse> reponses = new ArrayList<>();
        if(user.getFirstRole().getName().toString().equals(RoleEnum.CANDIDATE.toString())){
            List<Favourite> favourites = favouriteRepository.findByCandidateId(user.getId());
            favourites.stream().forEach( a -> {
                Post post = postRepository.findById(a.getPost_id()).orElseThrow(() -> new NotFoundException("Post not found!"));
                Optional<Application> application = applicationRepository.findByCandidateIdAndPostId(user.getId(), post.getId());
                Boolean isApply = true;
                if(application.isEmpty()){
                    isApply = false;
                }
                reponses.add(new SummaryPostForCandidateResponse(post.getId(),post.getName(),post.getCompany()== null ? null : post.getCompany().getName(),
                        post.getCompany()== null ? null : post.getCompany().getLogo(),a.getCreateAt(),true,isApply)
                );
            });
        }
        return reponses;
    }

    @Override
    public List<SummaryPostForRecruiterResponse> getMyJobForRecruiter(Boolean isShow) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        List<SummaryPostForRecruiterResponse> responses = new ArrayList<>();

        if(isShow == null){
            isShow = true;
        }
        List<Post> posts = postRepository.getMyJob(user.getId());
        if(isShow){
            List<Post> postFilter = posts.stream().filter(p -> p.getExpire().after(new Date())).collect(Collectors.toList());
            postFilter.stream().forEach(p -> {
                responses.add(new SummaryPostForRecruiterResponse(p.getId(), p.getName(), p.getCreateAt(), p.getExpire(), p.getMinSalary(), p.getMaxSalary()));
            });
        }else {
            posts.stream().forEach(p -> {
                responses.add(new SummaryPostForRecruiterResponse(p.getId(), p.getName(), p.getCreateAt(), p.getExpire(), p.getMinSalary(), p.getMaxSalary()));
            });
        }
        return responses;
    }

    @Override
    public BaseResponse<String> deleteJob(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        postRepository.delete(post);
        return new BaseResponse<String>(null, "Post deleted!");
    }

    @Override
    public PostResponse updatePost(PostRequest req, int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        post.setName(req.getName());


        post.setRequirement(req.getRequirements());
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());



        post.setMaxSalary(req.getMaxSalary());
        post.setMinSalary(req.getMinSalary());
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

        Set<Field> set = new HashSet<>();

        req.getField().stream().forEach(f -> {
            Field field = fieldRepository.findByName(f.getValue().toString()).orElseThrow(() -> new NotFoundException("Field not found!"));
            set.add(field);
        });
        post.setFields(set);


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));

        postRepository.save(post);
        return new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirement().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getMinSalary(), post.getMaxSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFields() == null ? null : post.getFields().stream().collect(Collectors.toList()),user.getFullName());

    }


}
