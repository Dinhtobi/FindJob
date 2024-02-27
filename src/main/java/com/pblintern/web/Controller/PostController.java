package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEER')")
    public ResponseEntity<?> addPost(@ModelAttribute @Valid PostRequest req){
        return ResponseEntity.ok(postService.addPost(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id){
        return ResponseEntity.ok(postService.getPost(id));
    }
}
