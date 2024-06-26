package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.*;

import java.util.List;

public interface CompanyService {
    List<CompanyCardResponse> getTopCompany(int size);

    List<CompanyCardDetailResponse> getCompanies(int size, String type);

    CompanyResponse getCompany(int id);

    List<PostCompanyResponse> getJobOfCompany(int id);

    List<CompanySelectResponse> getCompanySelect();

}
