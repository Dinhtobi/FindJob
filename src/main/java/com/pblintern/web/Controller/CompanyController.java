package com.pblintern.web.Controller;

import com.pblintern.web.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/top")
    public ResponseEntity<?> getTopCompany(@RequestParam(value = "size") int size){
        return ResponseEntity.ok(companyService.getTopCompany(size));
    }

    @GetMapping
    public ResponseEntity<?> getCompanies(@RequestParam(value = "size", required = false, defaultValue = "20")  int size, @RequestParam(value = "type", required = false) String type){
        return ResponseEntity.ok(companyService.getCompanies(size,type));
    }

    @GetMapping("/select")
    public ResponseEntity<?> getCompanies(){
        return ResponseEntity.ok(companyService.getCompanySelect());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompany(@PathVariable(value = "id") int id){
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobOfCompany(@PathVariable(value = "id") int id){
        return ResponseEntity.ok(companyService.getJobOfCompany(id));
    }
}
