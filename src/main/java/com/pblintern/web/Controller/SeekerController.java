package com.pblintern.web.Controller;


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

    @GetMapping
    public ResponseEntity<?> getAllSkill(){
        return ResponseEntity.ok(skillRepository.findAll());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<?> updateProfile(@PathVariable int id, SeekerRequest req){
        return ResponseEntity.ok(seekerService.updateSeeker(req,id));
    }
}
