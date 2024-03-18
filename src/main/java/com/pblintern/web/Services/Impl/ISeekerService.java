package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Seeker;
import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Entities.User;
import com.pblintern.web.Entities.WorkExperience;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Payload.Requests.SkillRequest;
import com.pblintern.web.Payload.Requests.UserRequest;
import com.pblintern.web.Payload.Responses.RegisterEmployeerResponse;
import com.pblintern.web.Payload.Responses.SeekerResponse;
import com.pblintern.web.Repositories.SeekerRepository;
import com.pblintern.web.Repositories.SkillRepository;
import com.pblintern.web.Repositories.UserRepository;
import com.pblintern.web.Repositories.WorkExperienceRepository;
import com.pblintern.web.Services.SeekerService;
import com.pblintern.web.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ISeekerService implements SeekerService {

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Override
    public SeekerResponse updateSeeker(SeekerRequest req, int id) {
        Seeker seeker = seekerRepository.findById(id).orElseThrow(()-> new NotFoundException("Seeker not found!"));

        User user = userService.updateUserInfo(new UserRequest(req.getFullName(),req.getPhoneNumber(), req.isGender(),req.getDateOfBirth(),req.getAvatar()), id);

        if(req.getAddress() != null)
            seeker.setAddress(req.getAddress());
        seekerRepository.save(seeker);
        return new SeekerResponse(seeker.getId(), user.getFullName(),user.getPhoneNumber(),user.getEmail(),user.getDateOfBirth().getTime(),
                user.isGender(),user.getAvatar(),seeker.getAddress(), seeker.getSkills() == null ? null :seeker.getSkills().stream().collect(Collectors.toList()),
                seeker.getWorkExperiences() == null ? null :seeker.getWorkExperiences().stream().collect(Collectors.toList()))  ;
    }

    @Override
    public SeekerResponse addCV(CVRequest cvRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Seeker not found!"));
        Seeker seeker = seekerRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Seeker not found!"));

        if(cvRequest.getSkills() == null ){
            seeker.setSkills(null);
        }else{
            Set<Skills> set = new HashSet<>();
            List<SkillRequest> skills_unavailable = cvRequest.getSkills().stream().filter( skill -> !skillRepository.findByName(skill.getName()).isPresent()).collect(Collectors.toList());
            skills_unavailable.stream().forEach(s -> {
                Skills skill = new Skills();
                skill.setName(s.getName());
                skillRepository.save(skill);
                set.add(skill);
            });
            cvRequest.getSkills().removeAll(skills_unavailable);
            cvRequest.getSkills().stream().forEach(s -> {
                Skills skill = skillRepository.findByName(s.getName()).get();
                set.add(skill);
            });
            seeker.setSkills(set);
        }
        if(cvRequest.getWorkExperiences() == null){
            seeker.setWorkExperiences(null);
        }else {

            Set<WorkExperience> workExperiences = new HashSet<>();
            cvRequest.getWorkExperiences().stream().forEach(w -> {
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
        return new SeekerResponse(seeker.getId(), user.getFullName(),user.getPhoneNumber(),user.getEmail(),user.getDateOfBirth().getTime(),
                user.isGender(),user.getAvatar(),seeker.getAddress(), seeker.getSkills() == null ? null :seeker.getSkills().stream().collect(Collectors.toList()),
                seeker.getWorkExperiences() == null ? null :seeker.getWorkExperiences().stream().collect(Collectors.toList()))  ;
    }
}
