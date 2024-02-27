package com.pblintern.web.Controller;

import com.pblintern.web.Services.HistorySeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historySearch")
public class HistorySearchController {

    @Autowired
    private HistorySeachService historySeachService;

    @PostMapping
    public ResponseEntity<?> addSearch(@RequestParam(value = "keyword" , required = true) String keyword){
        return ResponseEntity.ok(historySeachService.addSearch(keyword));
    }

    @GetMapping
    public ResponseEntity<?> getSearch(){
        return ResponseEntity.ok(historySeachService.getSearch());
    }

}
