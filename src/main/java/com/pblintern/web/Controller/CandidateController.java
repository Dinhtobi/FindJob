package com.pblintern.web.Controller;


import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.CandidateRequest;
import com.pblintern.web.Repositories.SkillRepository;
import com.pblintern.web.Services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private SkillRepository skillRepository;

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getProfile(){
        return ResponseEntity.ok(candidateService.getCandidate());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateProfile(@PathVariable int id, @ModelAttribute CandidateRequest req){
        return ResponseEntity.ok(candidateService.updateCandidate(req,id));
    }

    @GetMapping("/recommend")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> recommendCandidate(@RequestParam(value = "id") int id, @RequestParam(value = "all") Boolean all){
        return ResponseEntity.ok(candidateService.recommendCandidateForRecruiter(id,all));
    }


}
