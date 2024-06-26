package com.pblintern.web.Controller;

import com.pblintern.web.Services.CompanyService;
import com.pblintern.web.Services.PostService;
import com.pblintern.web.Services.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private PostService postService;

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getStatisticsForRecruiter(){
        return ResponseEntity.ok(statisticsService.statisticsForRecruiter());
    }

    @GetMapping("/statistics/chart")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getStatisticsForRecruiter(@RequestParam(required = false)  String startDate, @RequestParam(required = false)  String endDate, @RequestParam(required = false) String type){
        return ResponseEntity.ok(statisticsService.statisticsChart(startDate,endDate,type));
    }

    @GetMapping("/my-job")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getMyJobForRecruiter(@RequestParam(value = "isShow") Boolean isShow){
        return ResponseEntity.ok(postService.getMyJobForRecruiter(isShow));
    }
}
