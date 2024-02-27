package com.pblintern.web.Controller;

import com.pblintern.web.Services.HistoryInteractiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historyInteractive")
public class HistoryInteractiveController {

    @Autowired
    private HistoryInteractiveService historyInteractiveService;

    @PostMapping
    public ResponseEntity<?> addInteractive(@RequestParam(value = "id" ,required = true) int id){
        return ResponseEntity.ok(historyInteractiveService.addInteractive(id));
    }

    @GetMapping
    public ResponseEntity<?> getInteractive(){
        return ResponseEntity.ok(historyInteractiveService.getInteractive());
    }
}
