package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.RegisterEmployerRequest;
import com.pblintern.web.Payload.Requests.SeekerRequest;
import com.pblintern.web.Payload.Requests.UserRequest;
import com.pblintern.web.Services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/seeker")
    public ResponseEntity<?> registerSeekeer(@ModelAttribute @Valid SeekerRequest req){
        return ResponseEntity.ok(userService.registerSeeker(req));
    }

    @PostMapping("/employeer")
    public ResponseEntity<?> registerEmployeer(@ModelAttribute   @Valid RegisterEmployerRequest req){
        return ResponseEntity.ok(userService.registerEmployeer(req));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.loadUser(email));
    }
}
