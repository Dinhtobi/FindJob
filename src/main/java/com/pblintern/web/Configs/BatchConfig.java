package com.pblintern.web.Configs;

import com.pblintern.web.Batch.Processor.CSVExportProcessor;
import com.pblintern.web.Batch.Processor.RemoveSkillProcessor;
import com.pblintern.web.Batch.Reader.CSVExportReader;
import com.pblintern.web.Batch.Reader.RemoveSkillReader;
import com.pblintern.web.Batch.Writer.RemoveSkillWriter;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Payload.Requests.CSVRequest;
import com.pblintern.web.Payload.Responses.CsvResponse;
import com.pblintern.web.Repositories.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SkillRepository skillRepository;

    @Bean(name = "job-export-csv")
    public Job jobCSV(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-export-post",jobRepository)
                .start(stepExport(jobRepository,transactionManager))
                .preventRestart().build();
    }

    @Bean
    public Step stepExport(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step-export-post", jobRepository)
                .<CSVRequest,CSVRequest>chunk(5, transactionManager)
                .reader(new CSVExportReader(dataSource))
                .processor(new CSVExportProcessor())
                .writer(csvWriter())
                .build();
    }

    @Bean(name = "job-remove-skill")
    public Job jobRemoveSkill(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-remove-skill", jobRepository)
                .start(stepRemoveSkill(jobRepository,transactionManager))
                .preventRestart().build();
    }

    @Bean
    public Step stepRemoveSkill(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step-remove-skill", jobRepository)
                .<Skills,Skills>chunk(5,transactionManager)
                .reader(new RemoveSkillReader(skillRepository))
                .processor(new RemoveSkillProcessor())
                .writer(new RemoveSkillWriter(skillRepository))
                .build();
    }

    @Bean
    public FlatFileItemWriter<CSVRequest> csvWriter(){
        BeanWrapperFieldExtractor<CSVRequest> fieldExtractor = new BeanWrapperFieldExtractor<CSVRequest>();
        fieldExtractor.setNames(new String[] {"id", "name", "comanyId", "comanyName", "jobField", "salary", "exerience" , "level" ,"exire" ,"descrition" ,"requirements"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<CSVRequest> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return new FlatFileItemWriterBuilder<CSVRequest>()
                .name("CSVExportPost")
                .resource(new FileSystemResource("./file//ExportPost.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id;name;comanyId;comanyName;jobField;salary;exerience;level;exire;descrition;requirements"))
                .build();
    }
}
