package com.pblintern.web.Batch.Reader;

import com.pblintern.web.Repositories.PostRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Date;
import java.util.List;

public class NotificationRecruiterReader implements ItemReader<Integer> {

    private int index = -1;

    private final PostRepository postRepository;

    public NotificationRecruiterReader(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        index++;
        List<Integer> ids = postRepository.getIds(new Date());
        if(ids.size() <= index || ids == null){
            index = -1;
            return null;
        }
        return ids.get(index);
    }
}
