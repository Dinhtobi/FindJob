package com.pblintern.web.Batch.Reader;

import com.pblintern.web.Entities.Post;
import com.pblintern.web.Mapper.PostMapper;
import com.pblintern.web.Payload.Requests.CSVRequest;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CSVExportReader implements ItemReader<CSVRequest> {

    private final DataSource dataSource;

    public CSVExportReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private int index = 0;

    private List<CSVRequest> jobPosts = new ArrayList<>();

    @Override
    public CSVRequest read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        JdbcCursorItemReader<CSVRequest> itemReader = new JdbcCursorItemReader<CSVRequest>();
        if(index==0){
            itemReader.setDataSource(dataSource);
            itemReader.setSql("SELECT p.id, p.name,f.name as jobField, p.description as description ,p.requirement as requirement " +
                    "from post as p inner join post_field as pf on pf.post_id = p.id inner join field as f on f.id = pf.field_id ");
            itemReader.setRowMapper(new PostMapper());
            ExecutionContext executionContext = new ExecutionContext();
            itemReader.open(executionContext);
            CSVRequest post ;
            while((post = itemReader.read()) != null){
                final CSVRequest currentPost = post;
                Optional<CSVRequest> existingItem = jobPosts.stream()
                        .filter(item -> (item.getId() == currentPost.getId()))
                        .findFirst();
                if (existingItem.isPresent()) {
                    existingItem.get().setJobField(existingItem.get().getJobField() + ", " +post.getJobField());
                } else {
                    jobPosts.add(post);
                }
            }
        }
        if(index >= jobPosts.size()){
            return null;
        }
        index++;
        return jobPosts.get(index-1);
    }
}
