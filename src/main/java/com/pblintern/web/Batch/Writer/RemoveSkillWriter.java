package com.pblintern.web.Batch.Writer;

import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Repositories.SkillRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class RemoveSkillWriter implements ItemWriter<Skills> {

    private final SkillRepository skillRepository;

    public RemoveSkillWriter(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public void write(Chunk<? extends Skills> chunk) throws Exception {
        for(Skills skill : chunk){
            if(skill != null)
                skillRepository.delete(skill);
        }
    }
}
