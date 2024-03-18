package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.PostRequest;
import com.pblintern.web.Services.PostService;
import com.pblintern.web.Utils.Constant;
import jakarta.validation.Valid;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Date;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEER')")
    public ResponseEntity<?> addPost(@RequestBody PostRequest req){
        return ResponseEntity.ok(postService.addPost(req));
    }

    @GetMapping("/my-job")
    public ResponseEntity<?> getMyJob(){
        return ResponseEntity.ok(postService.getMyJob());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id){
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommendPostInteractive(){
        return ResponseEntity.ok(postService.recommend());
    }



    @GetMapping
    public ResponseEntity<?> getPost(@RequestParam(required = false) @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd")Date from,
                                     @RequestParam(required = false) @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd")Date to,
                                     @RequestParam(required = false) String searchField,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) String sortType,
                                     @RequestParam(required = false) String sortField,
                                     @RequestParam(required = false) String location,
                                     @RequestParam(required = false) String postingDate,
                                     @RequestParam(required = false) String experience,
                                     @RequestParam(required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) int size,
                                     @RequestParam(required = false, defaultValue = Constant.DEFAULT_PAGE_NUMBER) int number){
        return ResponseEntity.ok(postService.getJob(from,to ,searchField,keyword,sortType,sortField,location,postingDate,experience,size,number));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable("id") int id){
        return ResponseEntity.ok(postService.deleteJob(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateJob(@RequestBody PostRequest postRequest, @PathVariable("id") int id){
        return ResponseEntity.ok(postService.updatePost(postRequest,id));
    }
}
