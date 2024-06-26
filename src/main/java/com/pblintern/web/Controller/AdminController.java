package com.pblintern.web.Controller;

import com.pblintern.web.Services.CandidateService;
import com.pblintern.web.Services.RecruiterService;
import com.pblintern.web.Services.StatisticsService;
import com.pblintern.web.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private UserService userService;

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getStatisticsForAdmin(@RequestParam(required = false)  String startDate, @RequestParam(required = false)  String endDate, @RequestParam(required = false) String type){
        return ResponseEntity.ok(statisticsService.statisticForAmin(startDate,endDate,type));
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCandidateForAdmin(){
        return ResponseEntity.ok(candidateService.getCandidates());
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRecruiterForAdmin(){
        return ResponseEntity.ok(recruiterService.getRecruiters());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockAccountForAdmin(@PathVariable int id){
        return ResponseEntity.ok(userService.blockUser(id));
    }
}
