package com.pblintern.web.Configs;

import com.pblintern.web.Batch.Processor.CSVExportProcessor;
import com.pblintern.web.Batch.Processor.NotificationRecruiterProcessor;
import com.pblintern.web.Batch.Processor.RemoveRecruiterProcessor;
import com.pblintern.web.Batch.Reader.CSVExportReader;
import com.pblintern.web.Batch.Reader.NotificationRecruiterReader;
import com.pblintern.web.Batch.Reader.RemoveRecruiterReader;
import com.pblintern.web.Batch.Writer.NotificationRecruiterWriter;
import com.pblintern.web.Batch.Writer.RemoveRecruiterWriter;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Entities.Skills;
import com.pblintern.web.Payload.Requests.CSVRequest;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.EmailService;
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
    private UserRepository userRepository;
    @Autowired
    private  RecruiterRepository recruiterRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;

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


    @Bean
    public FlatFileItemWriter<CSVRequest> csvWriter(){
        BeanWrapperFieldExtractor<CSVRequest> fieldExtractor = new BeanWrapperFieldExtractor<CSVRequest>();
        fieldExtractor.setNames(new String[] {"id", "name","jobField","description" ,"requirements"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<CSVRequest> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return new FlatFileItemWriterBuilder<CSVRequest>()
                .name("CSVExportPost")
                .resource(new FileSystemResource("./file//ExportPost.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id;name;jobField;description;requirement"))
                .build();
    }

    @Bean
    public Step removeRecruiterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step-remove-recruiter", jobRepository)
                .<Integer,Integer>chunk(5, transactionManager)
                .reader(new RemoveRecruiterReader(userRepository))
                .processor(new RemoveRecruiterProcessor(emailService))
                .writer(new RemoveRecruiterWriter(userRepository,recruiterRepository))
                .build();
    }

    @Bean(name ="job-remove-recruiter")
    public Job removeRecruiterJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-remove-recruiter", jobRepository)
                .start(removeRecruiterStep(jobRepository,transactionManager))
                .preventRestart().build();
    }

    @Bean
    public Step notificationRecruiterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step-notification-recruiter", jobRepository)
                .<Integer, Post>chunk(5,transactionManager)
                .reader(new NotificationRecruiterReader(postRepository))
                .processor(new NotificationRecruiterProcessor(postRepository,notificationRepository))
                .writer(new NotificationRecruiterWriter(notificationRepository))
                .build();
    }

    @Bean(name="job-notification-recruiter")
    public Job notificationRecruiterJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-notification-recruiter", jobRepository)
                .start(notificationRecruiterStep(jobRepository, transactionManager))
                .preventRestart().build();
    }

}
