package com.pblintern.web.Batch.Reader;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Mapper.SkillMapper;
import com.pblintern.web.Repositories.SkillRepository;
import org.springframework.batch.item.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class RemoveSkillReader implements ItemReader<Skills> {

    private final SkillRepository skillRepository;

    public RemoveSkillReader(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    private int index = -1;

    @Override
    public Skills read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        index++;
        List<Skills>  skills = skillRepository.findAll();
        if(index >= skills.size() || skills == null){
            index = -1;
            return null;
        }
        return skills.get(index);
    }
}
