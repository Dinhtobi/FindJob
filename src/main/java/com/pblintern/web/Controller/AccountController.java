package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.RecruiterRequest;
import com.pblintern.web.Payload.Requests.CandidateRequest;
import com.pblintern.web.Services.RecruiterService;
import com.pblintern.web.Services.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping
    public ResponseEntity<?> getInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PostMapping("/candidate")
    public ResponseEntity<?> registerSeekeer(@ModelAttribute @Valid CandidateRequest req){
        return ResponseEntity.ok(userService.registerCandidate(req));
    }

    @PostMapping("/recruiter")
    public ResponseEntity<?> registerEmployeer(@ModelAttribute   @Valid RecruiterRequest req) throws UnsupportedEncodingException, MailException, MessagingException {
        return ResponseEntity.ok(userService.registerRecruiter(req));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.loadUser(email));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyRecruiter(@RequestParam("code") String code, @RequestParam("id") int id){
        return ResponseEntity.ok(recruiterService.activeAccount(code,id));
    }
}
