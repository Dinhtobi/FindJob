package com.pblintern.web.Services;

import com.pblintern.web.Entities.User;
import com.pblintern.web.Enums.RoleEnum;
import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.RegisterEmployerRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Payload.Requests.UserRequest;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.LoginResponse;
import com.pblintern.web.Payload.Responses.RegisterEmployeerResponse;
import com.pblintern.web.Payload.Responses.SeekerResponse;

public interface UserService {

    BaseResponse<LoginResponse> loadUser(String email);

    User registerUser(UserRequest userRequest , RoleEnum roleEnum);

    SeekerResponse registerSeeker(SeekerRequest seekerRequest);

    RegisterEmployeerResponse registerEmployeer(RegisterEmployerRequest registerEmployerRequest);

    User getById(int id);

    User getByEmail(String email);

    User updateUserInfo(UserRequest userRequest, int id);


}
