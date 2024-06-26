package com.pblintern.web.Configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {

//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    @Qualifier("job-export-csv")
//    private Job jobExportCsv;
//
//
//    @Autowired
//    @Qualifier("job-remove-recruiter")
//    private Job jobRemoveRecruiter;
//
//    @Autowired
//    @Qualifier("job-notification-recruiter")
//    private Job jobNotificationRecruiter;
//
//    @Scheduled(fixedRate = 20000)
//    public void runJobRemoveRecruiter() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("key", String.valueOf("Verification " + UUID.randomUUID())).toJobParameters();
//        jobLauncher.run(jobRemoveRecruiter,jobParameters);
//    }
//
//    @Scheduled(fixedRate = 1*60*60*1000)
//    public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
//      log.info("Run job CSV");
//        JobParameters jobParameter = new JobParametersBuilder()
//                .addString("key",String.valueOf("ExportCSV " + UUID.randomUUID())).toJobParameters();
//        jobLauncher.run(jobExportCsv,jobParameter);
//    }
//
//    @Scheduled(fixedRate = 20000)
//    public void runJobNotificationRecruiter() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("key", String.valueOf("Notification " + UUID.randomUUID())).toJobParameters();
//        jobLauncher.run(jobNotificationRecruiter,jobParameters);
//    }

}
