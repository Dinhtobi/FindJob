package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.DTO.RecommendPost;
import com.pblintern.web.Payload.DTO.RequirementPost;
import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Requests.RecommendRequest;
import com.pblintern.web.Payload.Requests.SkillRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.PagedResponse;
import com.pblintern.web.Payload.Responses.PostDetailResponse;
import com.pblintern.web.Payload.Responses.PostResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import com.pblintern.web.Services.PostService;
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
    private EmployeerRepository employeerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private HistoryInteractiveRepository historyInteractiveRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${AI.recommendUrl}")
    private String recommendUrl ;

    @Override
    public PostResponse addPost(PostRequest req) {
        Post post = new Post();
        post.setName(req.getName());

        String requirements = req.getRequirements().stream()
                .map(RequirementPost::getValue)
                .collect(Collectors.joining(", "));
        post.setRequirements(requirements);
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());


        String salary = "";
        salary += req.getMinSalary() +" - " + req.getMaxSalary() + " VND";
        post.setSalary(salary);
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
        Optional<Company> companyOptional = companyRepository.findByName(req.getCompanyName());
        if(!companyOptional.isPresent()){
            Company company = new Company();
            company.setName(req.getName());
            company.setLogo(req.getCompanyLogo());
            company.setLocation(req.getCompanyLocation());
            companyRepository.save(company);
            post.setCompany(company);
        }else{
            post.setCompany(companyOptional.get());
        }


        FieldOfActivity fieldOfActivity = fieldRepository.findByName(req.getField()).orElseThrow(() -> new NotFoundException("Field not found!"));
        post.setFieldOfActivity(fieldOfActivity);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        Employer employer = employeerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Employer not found!"));
        post.setEmployer(employer);
        postRepository.save(post);
        return new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName());
    }

    @Override
    public PostDetailResponse getPost(int id) {
        Post post =  postRepository.findById((id)).orElseThrow(() -> new NotFoundException("Post not found!"));
        User user = userRepository.findById(post.getEmployer().getId()).orElseThrow(() -> new NotFoundException("User not found"));
        String minSalary ="";
        String maxSalary = "";
        if(!post.getSalary().equals("") && !post.getSalary().equals("Cạnh tranh")){
            minSalary = post.getSalary().split(" ")[0] + " Tr";
            maxSalary = post.getSalary().split(" ")[3] + " Tr";
        }
        return new PostDetailResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName(),minSalary,maxSalary);
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
            PostResponse postResponse = new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName());
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
            PostResponse postResponse = new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName());
            posts.add(postResponse);
        });
        return posts;
    }


    @Override
    public List<PostResponse> recommend() {
        List<PostResponse> responses = recommendFromInteractive();
        List<PostResponse> recommend_unavailable = recommendFromSkill().stream().filter(r -> !responses.contains(r)).collect(Collectors.toList());
        responses.addAll(recommend_unavailable);
        return responses;
    }

    @Override
    public PagedResponse<PostResponse> getJob(Date from, Date to, String searchField, String keyword, String sortType, String sortField, String location,
                                              String postingDate,String experience,int size, int page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Post> root = criteriaQuery.from(Post.class);

        Join<Post,Employer> employerJoin = root.join("employer", JoinType.INNER);
        Join<Post,Company> companyJoin = root.join("company", JoinType.INNER);
        Join<Employer, User> userJoin = employerJoin.join("user", JoinType.INNER);
        criteriaQuery.multiselect(
                userJoin.alias("user"),
                employerJoin.alias("employer"),
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
            PostResponse postResponse = new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                    post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                    post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName());
            postResponses.add(postResponse);
        }
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<PostResponse> pageResponse = new PageImpl<PostResponse>(postResponses, pageRequest, totalResults);
        return new PagedResponse<PostResponse>(pageResponse.getContent(), pageResponse.getNumber(), pageResponse.getSize(),
                pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.isLast());
    }

    @Override
    public List<PostResponse> getMyJob() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        List<PostResponse> reponses = new ArrayList<>();
        if(user.getFirstRole().getName().toString().equals(RoleEnum.SEEKER.toString())){
            List<Application> apps = applicationRepository.findBySeekerId(user.getId());
            apps.stream().forEach( a -> {
                Post post = a.getJobPost();
                reponses.add(new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                        post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                        post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName())
                        );
            });
        }else if(user.getFirstRole().getName().toString().equals(RoleEnum.EMPLOYER.toString())){
            List<Post> posts = postRepository.getMyJob(user.getId());
            posts.stream().forEach(post -> {
                reponses.add(new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                        post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                        post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName())
                        );
            });
        }
        return reponses;
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

        String requirements = req.getRequirements().stream()
                .map(RequirementPost::getValue)
                .collect(Collectors.joining(", "));
        post.setRequirements(requirements);
        post.setLevel(req.getLevel());
        post.setDescription(req.getDescription());


        String salary = "";
        salary += req.getMinSalary() +" - " + req.getMaxSalary() + " VND";
        post.setSalary(salary);
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
        Optional<Company> companyOptional = companyRepository.findByName(req.getCompanyName());
        if(!companyOptional.isPresent()){
            Company company = new Company();
            company.setName(req.getName());
            company.setLogo(req.getCompanyLogo());
            company.setLocation(req.getCompanyLocation());
            companyRepository.save(company);
            post.setCompany(company);
        }else{
            post.setCompany(companyOptional.get());
        }


        FieldOfActivity fieldOfActivity = fieldRepository.findByName(req.getField()).orElseThrow(() -> new NotFoundException("Field not found!"));
        post.setFieldOfActivity(fieldOfActivity);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));

        postRepository.save(post);
        return new PostResponse(post.getId(),post.getName(),post.getLevel(),post.getDescription().replaceAll("\n"," ").replaceAll("-",""),post.getExperience(),post.getRequirements().replaceAll("\n", " ").replaceAll("-",""),
                post.getCreateAt() == null ? null : post.getCreateAt(),post.getExpire() == null ? null : post.getExpire(), post.getSalary(),post.getCompany()== null ? null : post.getCompany().getName(),
                post.getCompany()== null ? null : post.getCompany().getLogo(),post.getCompany()== null ? null : post.getCompany().getLocation(),post.getFieldOfActivity().getName(),user.getFullName());

    }
}
