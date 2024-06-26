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

    @GetMapping("/candidate")
    public ResponseEntity<?> getCandidateOfApplication(@RequestParam(required = true) int id, @RequestParam(required = true) String filter){
        return ResponseEntity.ok(applicationService.getApplicationOfPost(id,filter));
    }

    @PostMapping
    public ResponseEntity<?> addApplication(@RequestParam(required = true) int id){
        return ResponseEntity.ok(applicationService.addApplication(id));
    }

    @PutMapping
    public ResponseEntity<?> updateViewApplication(@RequestParam(required = true) int candidateId , @RequestParam(required = true) int postId){
        return ResponseEntity.ok(applicationService.updateViewApplication(candidateId, postId));
    }

    @PatchMapping
    public ResponseEntity<?> updateViewApplication(@RequestParam(required = true) int candidateId , @RequestParam(required = true) int postId , @RequestParam(required = true) String status){
        return ResponseEntity.ok(applicationService.updateStatusApplication(candidateId, postId, status));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteApplication(@RequestParam(required = true) int id){
        return ResponseEntity.ok(applicationService.deleteApplication(id));
    }
}
