package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Recruiter;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class IEmailService implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Value("${Email.siteUrl}")
    private String siteUrl;

    @Value("${Email.fromAddress}")
    private String fromAddress;

    @Override
    public void sendRemoveAccount(int id) throws UnsupportedEncodingException, MailException, MessagingException {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
        String content = "Dear [[name]],<br>"
                + "You didn't verify account, so we removed your account!:<br>"
                + "Thank you,<br>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, "Remove account recruiter");
        helper.setTo(recruiter.getUser().getEmail());
        helper.setSubject("Notice of account deletion");

        content = content.replace("[[name]]", recruiter.getUser().getFullName());

        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendVerificationRecruiter(int id) throws UnsupportedEncodingException,MailException,MessagingException {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new NotFoundException("Recruiter not found!"));
        String content = "Dear [[nameCompany]],<br>"
                + "We received a request to register as a recruiter for your company <br>"
                + "Please verify your employee information and click on the link below to complete registration <br>"
                + "Your recruiter information <br>"
                + "    * Name: [[name]] <br>"
                + "    * Email:[[email]] <br>"
                + "    * Position: [[position]] <br>"
                + "    * phoneNumber: [[phoneNumber]] <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, "Verification account recruiter");
        helper.setTo(recruiter.getCompany().getEmail());
        helper.setSubject("Please verify your registration");
        String verifyUrl = siteUrl + "/verify?code=" + recruiter.getVerification_code() +"&id=" + recruiter.getId();

        content = content.replace("[[nameCompany]]", recruiter.getCompany().getName())
                .replace("[[name]]", recruiter.getUser().getFullName())
                .replace("[[email]]", recruiter.getUser().getEmail())
                .replace("[[position]]", recruiter.getPosition())
                .replace("[[phoneNumber]]", recruiter.getUser().getPhoneNumber())
                .replace("[[URL]]", verifyUrl);

        helper.setText(content, true);
        mailSender.send(message);
    }
}
