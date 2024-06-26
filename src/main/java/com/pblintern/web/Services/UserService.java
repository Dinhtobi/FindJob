package com.pblintern.web.Services;

import com.pblintern.web.Entities.User;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Payload.Requests.RecruiterRequest;
import com.pblintern.web.Payload.Requests.CandidateRequest;
import com.pblintern.web.Payload.Requests.UserRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.LoginResponse;
import com.pblintern.web.Payload.Responses.RecruiterResponse;
import com.pblintern.web.Payload.Responses.CandidateResponse;
import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

import java.io.UnsupportedEncodingException;

public interface UserService {

    BaseResponse<?> loadUser(String email);

    User registerUser(UserRequest userRequest , RoleEnum roleEnum);

    CandidateResponse registerCandidate(CandidateRequest candidateRequest);

    RecruiterResponse registerRecruiter(RecruiterRequest recruiterRequest) throws UnsupportedEncodingException, MailException, MessagingException;

    User getById(int id);

    User getByEmail(String email);

    User updateUserInfo(UserRequest userRequest, int id);

    BaseResponse<Boolean> blockUser(int id);
}
