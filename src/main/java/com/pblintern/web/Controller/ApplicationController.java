package com.pblintern.web.Controller;

import com.pblintern.web.Services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> addApplication(@RequestParam(required = true) int id){
        return ResponseEntity.ok(applicationService.addApplication(id));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteApplication(@RequestParam(required = true) int id){
        return ResponseEntity.ok(applicationService.deleteApplication(id));
    }
}
