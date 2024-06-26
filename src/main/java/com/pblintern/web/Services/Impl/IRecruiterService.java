package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Recruiter;
import com.pblintern.web.Entities.User;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.RecruiterForAdminResponse;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Services.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IRecruiterService implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public BaseResponse<Boolean> activeAccount(String code, int id) {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
        if(recruiter.getVerification_code().equals(code)){
            recruiter.setEnable(true);
            recruiter.setVerification_code(null);
            recruiterRepository.save(recruiter);
            return new BaseResponse<>(true, "Verify complete!");
        }else{
            throw new BadRequestException("code wrong!");
        }
    }

    @Override
    public List<RecruiterForAdminResponse> getRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findAll();
        List<RecruiterForAdminResponse> response = new ArrayList<>();
        recruiters.stream().forEach(r -> {
            User user = r.getUser();
            response.add(new RecruiterForAdminResponse(r.getId(),user.getFullName(), user.getEmail(), r.getPosition(), user.isNonBlock()));
        });
        return response;

    }
}
