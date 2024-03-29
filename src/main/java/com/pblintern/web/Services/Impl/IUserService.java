package com.pblintern.web.Services.Impl;


import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.RegisterEmployerRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Payload.Requests.SkillRequest;
import com.pblintern.web.Payload.Requests.UserRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.LoginResponse;
import com.pblintern.web.Payload.Responses.RegisterEmployeerResponse;
import com.pblintern.web.Payload.Responses.SeekerResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private EmployeerRepository employeerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private IFileStorageService fileStorageService;

    @Override
    public BaseResponse<LoginResponse> loadUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return new BaseResponse<LoginResponse>(new LoginResponse("Unregister ",null, -1 ), "User not found");
        }
        User user = userOptional.get();
        if(!user.isNonBlock()){
            return new BaseResponse<LoginResponse>(new LoginResponse("Blocked", user.getFirstRole().getName(), user.getId()), "User is blocked");
        }
        return new BaseResponse<LoginResponse>(new LoginResponse("Success", user.getFirstRole().getName(),user.getId()), "User is registered");
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
        User user = new User();
        Date dateNow = new Date();
        user.setCreateAt(dateNow);
        user.setNonBlock(true);
        user.setFullName(registerUserRequest.getFullName());
        user.setPhoneNumber(registerUserRequest.getPhoneNumber());
        user.setEmail(registerUserRequest.getEmail());
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
        if(registerUserRequest.getAvatar() == null && roleEnum.equals(RoleEnum.EMPLOYER)){
            throw new BadRequestException("Avatar not NULL!");
        }
        if(registerUserRequest.getAvatar() != null){
            String url = fileStorageService.createImgUrl(registerUserRequest.getAvatar());
            user.setAvatar(url);
        }
        return user;
    }

    @Override
    @Transactional
    public SeekerResponse registerSeeker(SeekerRequest registerSeekerRequest) {
        Optional<User> user = userRepository.findByEmail(registerSeekerRequest.getEmail());
        if(user.isPresent()){
            throw new BadRequestException("User has been already registered");
        }
        User userSaved = user.orElseGet(() -> registerUser(new UserRequest(registerSeekerRequest.getFullName(),
                                                                            registerSeekerRequest.getPhoneNumber(),
                                                                            registerSeekerRequest.getEmail(),
                                                                            registerSeekerRequest.isGender(),
                                                                            registerSeekerRequest.getDateOfBirth(),
                                                                            registerSeekerRequest.getAvatar()
                                                                            ),RoleEnum.SEEKER));
        userRepository.save(userSaved);
        Seeker seeker = new Seeker();
        seeker.setUser(userSaved);
        seeker.setId(userSaved.getId());
        seeker.setAddress(registerSeekerRequest.getAddress());
        if(registerSeekerRequest.getSkills() == null ){
            seeker.setSkills(null);
        }else{
            Set<Skills> set = new HashSet<>();
            List<SkillRequest> skills_unavailable = registerSeekerRequest.getSkills().stream().filter( skill -> !skillRepository.findByName(skill.getName()).isPresent()).collect(Collectors.toList());
            skills_unavailable.stream().forEach(s -> {
                Skills skill = new Skills();
                skill.setName(s.getName());
                skillRepository.save(skill);
                set.add(skill);
            });
            registerSeekerRequest.getSkills().removeAll(skills_unavailable);
            registerSeekerRequest.getSkills().stream().forEach(s -> {
                Skills skill = skillRepository.findByName(s.getName()).get();
                set.add(skill);
            });
            seeker.setSkills(set);
        }
        if(registerSeekerRequest.getWorkExperiences() == null){
            seeker.setWorkExperiences(null);
        }else {

            Set<WorkExperience> workExperiences = new HashSet<>();
            registerSeekerRequest.getWorkExperiences().stream().forEach(w -> {
                WorkExperience workExperience = new WorkExperience();
                workExperience.setName(w.getName());
                workExperience.setPosition(w.getPosition());
                workExperience.setDescription(w.getDescription());
                if (w.getTimeStart() != null) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(w.getTimeStart());
                        workExperience.setTimeStart(date);
                    } catch (Exception e) {
                        workExperience.setTimeStart(null);
                    }
                } else
                    workExperience.setTimeStart(null);
                if (w.getTimeEnd() != null) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(w.getTimeEnd());
                        workExperience.setTimeEnd(date);
                    } catch (Exception e) {
                        workExperience.setTimeEnd(null);
                    }
                } else
                    workExperience.setTimeEnd(null);
                workExperiences.add(workExperience);
                workExperienceRepository.save(workExperience);
            });
            seeker.setWorkExperiences(workExperiences);
         }
        seekerRepository.save(seeker);
        return new SeekerResponse(seeker.getId(), userSaved.getFullName(),userSaved.getPhoneNumber(),userSaved.getEmail(),userSaved.getDateOfBirth().getTime(),
                                            userSaved.isGender(),userSaved.getAvatar(),seeker.getAddress(), seeker.getSkills() == null ? null :seeker.getSkills().stream().collect(Collectors.toList()),
                                            seeker.getWorkExperiences() == null ? null :seeker.getWorkExperiences().stream().collect(Collectors.toList()))  ;

    }

    @Override
    @Transactional
    public RegisterEmployeerResponse registerEmployeer(RegisterEmployerRequest registerEmployerRequest) {
        Optional<User> user = userRepository.findByEmail(registerEmployerRequest.getEmail());
        if(user.isPresent()){
            throw new BadRequestException("User has been already registered");
        }
        User userSaved = user.orElseGet(() -> registerUser(new UserRequest(registerEmployerRequest.getFullName(),
                registerEmployerRequest.getPhoneNumber(),
                registerEmployerRequest.getEmail(),
                registerEmployerRequest.isGender(),
                registerEmployerRequest.getDateOfBirth(),
                registerEmployerRequest.getAvatar()
                ),RoleEnum.EMPLOYER));
        userRepository.save(userSaved);
        Employer employer = new Employer();
        employer.setUser(userSaved);
        employer.setId(userSaved.getId());
        employer.setPosition(registerEmployerRequest.getPosition());
        employeerRepository.save(employer);
        return new RegisterEmployeerResponse(employer.getId(), userSaved.getFullName(),userSaved.getPhoneNumber(),userSaved.getEmail(),userSaved.getDateOfBirth().getTime(),
                userSaved.isGender(),userSaved.getAvatar(),employer.getPosition()) ;
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
            String url = fileStorageService.createImgUrl(userRequest.getAvatar());
            user.setAvatar(url);
        }
        userRepository.save(user);
        return user;
    }
}
