package com.pblintern.web.Batch.Processor;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Repositories.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
@Slf4j
public class RemoveSkillProcessor implements ItemProcessor<Skills,Skills> {

    @Override
    public Skills process(Skills item) throws Exception {
        if(!item.getSeekers().isEmpty()){
            log.info("Name skill {}" , item.getName());
            return null;
        }else{
            return item;
        }
    }
}
