package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Company;
import com.pblintern.web.Entities.Employer;
import com.pblintern.web.Entities.FieldOfActivity;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Requests.RegisterCompanyRequest;
import com.pblintern.web.Payload.Responses.CompanyResponse;
import com.pblintern.web.Repositories.CompanyRepository;
import com.pblintern.web.Repositories.EmployeerRepository;
import com.pblintern.web.Repositories.FieldRepository;
import com.pblintern.web.Services.CompanyService;
import com.pblintern.web.Services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ICompanyService implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeerRepository employeerRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public CompanyResponse registerCompany(RegisterCompanyRequest req) {
        Company company = new Company();
        company.setName(req.getName());
        company.setWebSite(req.getWebSite());
        company.setLocation(req.getLocations());
        company.setEmail(req.getEmail());
        company.setPhoneNumber(req.getPhoneNumber());
        company.setCompanySize(req.getCompanySize());
        company.setCompanyType(req.getCompanyType());
        company.setTaxCode(req.getTaxCode());
        company.setDescription(req.getDescription());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employeerRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Employer not found!"));
        String fileName="";
        String logo ="";
        if(req.getLogo() != null){
            logo = fileStorageService.createImgUrl(req.getLogo());
        }
        company.setLogo(logo);
        for(int i = 0 ; i < req.getBusinessLicense().length ; i++){
            fileName = fileStorageService.createRootImgUrl(req.getBusinessLicense()[i],req.getPhoneNumber(),i);
        }
        company.setBusinessLicenseImg(fileName);
        companyRepository.save(company);
        employer.setIdCompany(company.getId());
        employeerRepository.save(employer);
        return new CompanyResponse(company.getName(), company.getWebSite(), company.getLocation(), company.getCompanySize(),
                company.getCompanyType(),company.getEmail(),company.getPhoneNumber(),company.getDescription(),company.getLogo(),company.getBusinessLicenseImg());
    }
}
