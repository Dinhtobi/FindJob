package com.pblintern.web.Batch.Reader;

import com.pblintern.web.Entities.Post;
import com.pblintern.web.Mapper.PostMapper;
import com.pblintern.web.Payload.Requests.CSVRequest;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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
            itemReader.setSql("SELECT p.id, p.name, c.id as company_id, c.name as company_name,f.name as job_field,p.salary, p.experience, " +
                    " p.level, p.expire, p.description," +
                    " p.requirements from post as p inner join company as c on p.company_id = c.id " +
                    " inner join field_of_activity as f on f.id = p.field_id");
            itemReader.setRowMapper(new PostMapper());
            ExecutionContext executionContext = new ExecutionContext();
            itemReader.open(executionContext);
            CSVRequest post ;
            while((post = itemReader.read()) != null){
                jobPosts.add(post);
            }
        }
        if(index >= jobPosts.size()){
            return null;
        }
        index++;
        return jobPosts.get(index-1);
    }
}
