package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Repositories.SkillRepository;
import com.pblintern.web.Services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ISkillService implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public List<Skills> getSkills() {
        List<Skills> skills = skillRepository.findAll();
        if(skills.isEmpty())
            throw new NotFoundException("Skills is empty!");
        return skills;
    }
}
