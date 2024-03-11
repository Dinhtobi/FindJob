package com.pblintern.web.Services;

import com.pblintern.web.Entities.User;
import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Payload.Responses.SeekerResponse;

public interface SeekerService {
    SeekerResponse updateSeeker(SeekerRequest req, int id);
    SeekerResponse addCV(CVRequest cvRequest);
}
