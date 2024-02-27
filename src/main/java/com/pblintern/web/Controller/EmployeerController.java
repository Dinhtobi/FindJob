package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Payload.Requests.RegisterCompanyRequest;
import com.pblintern.web.Services.CompanyService;
import com.pblintern.web.Services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employeer")
public class EmployeerController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/company")
    @PreAuthorize("hasRole('EMPLOYEER')")
    public ResponseEntity<?> registerCompany(@ModelAttribute @Valid RegisterCompanyRequest req){
        return ResponseEntity.ok(companyService.registerCompany(req));
    }

}
