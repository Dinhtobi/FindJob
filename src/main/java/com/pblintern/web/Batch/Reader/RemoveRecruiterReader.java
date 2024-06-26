package com.pblintern.web.Batch.Reader;

import com.pblintern.web.Repositories.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Date;
import java.util.List;

public class RemoveRecruiterReader implements ItemReader<Integer> {

    private int index = -1;

    private final UserRepository userRepository;

    public RemoveRecruiterReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        index++ ;
        List<Integer> ids = userRepository.getIds(new Date(System.currentTimeMillis() - 30*60000));
        if( index >= ids.size() || ids == null){
            index = -1;
            return null;
        }
        return ids.get(index);
    }
}
