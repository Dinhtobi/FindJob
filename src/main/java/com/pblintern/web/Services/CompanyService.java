package com.pblintern.web.Services;

import com.pblintern.web.Payload.Requests.RegisterCompanyRequest;
import com.pblintern.web.Payload.Responses.CompanyResponse;

public interface CompanyService {
    CompanyResponse registerCompany(RegisterCompanyRequest requestCompanyRequest);
}
