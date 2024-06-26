package com.pblintern.web.Services;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    public void sendRemoveAccount(int id) throws UnsupportedEncodingException, MailException,MessagingException;

    public void sendVerificationRecruiter(int id) throws UnsupportedEncodingException,MailException,MessagingException;
}
