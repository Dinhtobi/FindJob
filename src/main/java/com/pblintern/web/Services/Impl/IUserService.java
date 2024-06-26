package com.pblintern.web.Services.Impl;


import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.*;
import com.pblintern.web.Payload.Responses.*;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IStorageService storageService;

    @Override
    public BaseResponse<?> loadUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return new BaseResponse<LoginResponse>(new LoginResponse("Unregister ",null, -1 ), "User not found");
        }
        User user = userOptional.get();
        if(!user.isNonBlock()){
            return new BaseResponse<LoginResponse>(new LoginResponse("Blocked", user.getFirstRole().getName(), user.getId()), "User is blocked");
        }
        String avatar = storageService.createPresignedGetUrl("avatarfindjob", user.getAvatar()).getUrl();
        return new BaseResponse<UserResponse>(new UserResponse("Success", user.getFirstRole().getName(),user.getId(), user.getFullName(), user.getPhoneNumber(), user.getEmail(), avatar, user.isGender(), user.getDateOfBirth()), "Login success");
    }

    @Override
    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    public User registerUser(UserRequest registerUserRequest , RoleEnum roleEnum) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User();
        Date dateNow = new Date();
        user.setCreateAt(dateNow);
        user.setNonBlock(true);
        user.setFullName(registerUserRequest.getFullName());
        user.setPhoneNumber(registerUserRequest.getPhoneNumber());
        user.setEmail(email);
        user.setGender(registerUserRequest.isGender());
        if (registerUserRequest.getDateOfBirth() != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(registerUserRequest.getDateOfBirth());
                user.setDateOfBirth(date);
            } catch (Exception e) {
                user.setDateOfBirth(null);
            }
        } else
            user.setDateOfBirth(null);
        Role role = roleRepository.findByName(roleEnum).orElseThrow(() -> new NotFoundException("Role not found!"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }

    @Override
    @Transactional
    public CandidateResponse registerCandidate(CandidateRequest registerCandidateRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new BadRequestException("User has been already registered");
        }
        User userSaved = user.orElseGet(() -> registerUser(new UserRequest(registerCandidateRequest.getFullName(),
                                                                            registerCandidateRequest.getPhoneNumber(),
                                                                            registerCandidateRequest.isGender(),
                                                                            registerCandidateRequest.getDateOfBirth(),
                                                                            registerCandidateRequest.getAvatar()
                                                                            ),RoleEnum.CANDIDATE));
        userRepository.save(userSaved);
        userSaved.setAvatar(String.valueOf(userSaved.getId()));
        userRepository.save(userSaved);
        Candidate candidate = new Candidate();
        if(registerCandidateRequest.getSkills() == null ){
            candidate.setSkills(null);
        }else{
            Set<Skills> set = new HashSet<>();
            List<SkillRequest> skills = new ArrayList<>();
            registerCandidateRequest.getSkills().stream().forEach(r -> {
                skills.add(new SkillRequest(r.getValue()));
            });
            List<SkillRequest> skills_unavailable = skills.stream().filter( s -> !skillRepository.findByName(s.getName()).isPresent()).collect(Collectors.toList());
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
        Field field = fieldRepository.findById(registerCandidateRequest.getFieldId()).orElseThrow(() -> new NotFoundException("Field not found!"));
        candidate.setField(field);
        candidate.setUser(userSaved);
        candidate.setId(userSaved.getId());
        candidate.setAddress(registerCandidateRequest.getAddress());
        candidate.setCvUrl(String.valueOf(userSaved.getId()));
        candidateRepository.save(candidate);
        String avatarUrl = storageService.createPresignedGetUrl("avatarfindjob", userSaved.getAvatar()).getUrl();
        String cvUrl = storageService.createPresignedGetUrl("cvfindjob", candidate.getCvUrl()).getUrl();
        return new CandidateResponse(candidate.getId(), userSaved.getFullName(),userSaved.getPhoneNumber(),userSaved.getEmail(),userSaved.getDateOfBirth().getTime(),
                                            userSaved.isGender(),avatarUrl, candidate.getAddress(), candidate.getSkills() == null ? null : candidate.getSkills().stream().collect(Collectors.toList()),cvUrl,candidate.getField());


    }

    @Override
    @Transactional
    public RecruiterResponse registerRecruiter(RecruiterRequest recruiterRequest) throws UnsupportedEncodingException, MailException, MessagingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new BadRequestException("User has been already registered");
        }
        User userSaved = user.orElseGet(() -> registerUser(new UserRequest(recruiterRequest.getFullName(),
                recruiterRequest.getPhoneNumber(),
                recruiterRequest.isGender(),
                recruiterRequest.getDateOfBirth(),
                recruiterRequest.getAvatar()
        ), RoleEnum.RECRUITER));
        userRepository.save(userSaved);
        userSaved.setAvatar(String.valueOf(userSaved.getId()));
        userRepository.save(userSaved);
        Recruiter recruiter = new Recruiter();
        recruiter.setUser(userSaved);
        recruiter.setId(userSaved.getId());
        recruiter.setPosition(recruiterRequest.getPosition());
        recruiter.setEnable(false);
        recruiter.setVerification_code(UUID.randomUUID().toString().substring(0, 6));
        Company company;
        if(!recruiterRequest.getIsHadCompany()){
                company = new Company();
                company.setName(recruiterRequest.getCompanyName());
                company.setCompanyType(recruiterRequest.getCompanyType());
                company.setCompanySize(recruiterRequest.getCompanySize());
                company.setLogo(recruiterRequest.getCompanyLogo());
                company.setLocation(recruiterRequest.getCompanyLocation());
                company.setDescription(recruiterRequest.getCompanyDescription());
                company.setEmail(recruiterRequest.getCompanyEmail());
                company.setWebSite(recruiterRequest.getCompanyWebSite());
                companyRepository.save(company);
                company.setBusinessLicenseImg(String.valueOf(company.getId()));
                companyRepository.save(company);
                recruiter.setCompany(company);
        }else{
            company = companyRepository.findById(recruiterRequest.getCompanyId()).orElseThrow(() -> new NotFoundException("Company not found!"));
            recruiter.setCompany(company);
        }

        recruiterRepository.save(recruiter);

        emailService.sendVerificationRecruiter(recruiter.getId());
        return new RecruiterResponse(recruiter.getId(), userSaved.getFullName(), userSaved.getPhoneNumber(), userSaved.getEmail(), userSaved.getDateOfBirth().getTime(),
                userSaved.isGender(), userSaved.getAvatar(), recruiter.getPosition(), company.getId());
    }
    @Override
    public User updateUserInfo(UserRequest userRequest, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        user.setFullName(userRequest.getFullName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setGender(userRequest.isGender());
        if (userRequest.getDateOfBirth() != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(userRequest.getDateOfBirth());
                user.setDateOfBirth(date);
            } catch (Exception e) {
                user.setDateOfBirth(null);
            }
        }
        if(userRequest.getAvatar() != null){
            user.setAvatar(userRequest.getAvatar());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public BaseResponse<Boolean> blockUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        if(user.isNonBlock()){
            user.setNonBlock(false);
        }else{
            user.setNonBlock(true);
        }
        userRepository.save(user);
        return new BaseResponse<Boolean>(true, "Update success!");
    }
}
