package com.pblintern.web.Batch.Writer;

import com.pblintern.web.Entities.Recruiter;
import com.pblintern.web.Entities.User;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class RemoveRecruiterWriter implements ItemWriter<Integer> {

    private final UserRepository userRepository;

    private final RecruiterRepository recruiterRepository;

    public RemoveRecruiterWriter(UserRepository userRepository, RecruiterRepository recruiterRepository) {
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public void write(Chunk<? extends Integer> chunk) throws Exception {
        for(Integer id : chunk){
            User user = userRepository.findById(id).get();
            Recruiter recruiter = recruiterRepository.findById(id).get();
            recruiterRepository.delete(recruiter);
            userRepository.delete(user);
            log.info("User with id = {} deleted", id);
        }
    }
}
