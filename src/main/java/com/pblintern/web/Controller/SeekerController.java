package com.pblintern.web.Controller;


import com.pblintern.web.Payload.Requests.CVRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Repositories.SkillRepository;
import com.pblintern.web.Services.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seeker")
public class SeekerController {

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private SkillRepository skillRepository;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<?> updateProfile(@PathVariable int id, SeekerRequest req){
        return ResponseEntity.ok(seekerService.updateSeeker(req,id));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<?> addCV(@RequestBody CVRequest cvRequest){
        return ResponseEntity.ok(seekerService.addCV(cvRequest));
    }
}
