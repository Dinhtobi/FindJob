package com.pblintern.web.Batch.Processor;

import com.pblintern.web.Services.EmailService;
import org.springframework.batch.item.ItemProcessor;

public class RemoveRecruiterProcessor implements ItemProcessor<Integer, Integer> {

    private final EmailService emailService;

    public RemoveRecruiterProcessor(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public Integer process(Integer item) throws Exception {
        emailService.sendRemoveAccount(item);
        return item;
    }
}
