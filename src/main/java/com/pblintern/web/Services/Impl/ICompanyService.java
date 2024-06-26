package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.Company;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.*;
import com.pblintern.web.Repositories.CompanyRepository;
import com.pblintern.web.Repositories.PostRepository;
import com.pblintern.web.Repositories.RecruiterRepository;
import com.pblintern.web.Repositories.FieldRepository;
import com.pblintern.web.Repositories.projection.CompanyDetailProjection;
import com.pblintern.web.Repositories.projection.CompanyProjection;
import com.pblintern.web.Repositories.projection.CompanySelectProjection;
import com.pblintern.web.Services.CompanyService;
import com.pblintern.web.Utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ICompanyService implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public List<CompanyCardResponse> getTopCompany(int size) {
        List<CompanyProjection> projection = companyRepository.getTopCompany(size);
        if (projection == null)
            throw new BadRequestException("Companys is empty");
        List<CompanyCardResponse> cards = new ArrayList<>();
        projection.stream().forEach(p -> {
            cards.add(new CompanyCardResponse(p.getId(), p.getName(), p.getLogo()));
        });
        return cards;
    }

    @Override
    public List<CompanyCardDetailResponse> getCompanies(int size, String type) {
        List<CompanyDetailProjection> projection = new ArrayList<>();
        if(type == null) {
            type = "all";
        }
        if(type.equals("top")){
            projection = companyRepository.getTopCompanyDetail(size);
        }else{
            projection = companyRepository.getCompanies();
        }

        if (projection == null)
            throw new BadRequestException("Companys is empty");
        List<CompanyCardDetailResponse> cards = new ArrayList<>();
        projection.stream().forEach(p -> {
            cards.add(new CompanyCardDetailResponse(p.getId(), p.getName(), p.getLogo(), p.getSize(), p.getType(), p.getLocation()));
        });
        return cards;
    }

    @Override
    public CompanyResponse getCompany(int id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new NotFoundException("Company not found!"));
        return new CompanyResponse(company.getName(), company.getWebSite(), company.getLocation(), company.getCompanySize(), company.getCompanyType(), company.getDescription(), company.getLogo() );
    }

    @Override
    public List<PostCompanyResponse> getJobOfCompany(int id) {
        List<Post> posts = postRepository.getJobOfCompany(id);
        if(posts.isEmpty()){
            throw new NotFoundException("Post of Company is empty!");
        }
        List<PostCompanyResponse> response = new ArrayList<>();
        posts.stream().forEach(p -> {
            PostCompanyResponse postCompany = new PostCompanyResponse(p.getId(),p.getName(), p.getCreateAt(),p.getExpire(),p.getExperience(),p.getMinSalary(), p.getMaxSalary(),p.getCompany().getName() == null ? "" :p.getCompany().getName(), p.getCompany().getLogo() == null ? "" : p.getCompany().getLogo());
            response.add(postCompany);
        });
        return response;
    }

    @Override
    public List<CompanySelectResponse> getCompanySelect() {
        List<CompanySelectProjection> projections = companyRepository.getCompanySelect();
        if(projections.isEmpty())
            throw new NotFoundException("Company is empty!");
        List<CompanySelectResponse> companies = new ArrayList<>();
        projections.stream().forEach(p -> {
            companies.add(new CompanySelectResponse(p.getId(), p.getName()));
        });
        return companies;
    }
}
