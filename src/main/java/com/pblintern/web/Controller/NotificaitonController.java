package com.pblintern.web.Controller;

import com.pblintern.web.Payload.Requests.NotificationRequest;
import com.pblintern.web.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificaitonController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotification(@RequestParam(name = "size" , defaultValue = "10") int size){
        return ResponseEntity.ok(notificationService.getNotifications(size));
    }

    @GetMapping("/model")
    public ResponseEntity<?> getModelNotification(@RequestParam(name = "postId" , defaultValue = "0") int postId){
        return ResponseEntity.ok(notificationService.getModelNotification(postId));
    }

    @PostMapping
    public ResponseEntity<?> addNotification(@RequestBody NotificationRequest req){
        return ResponseEntity.ok(notificationService.addNotification(req));
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteApplyNotification(@RequestParam(value = "id", defaultValue = "0") int id, @RequestParam(value = "postId", defaultValue = "0") int postId){
        return ResponseEntity.ok(notificationService.inviteApplyNotification(id,postId));
    }

    @PutMapping
    public ResponseEntity<?> updateNotification(@RequestParam(name = "id" , defaultValue = "0") int id){
        return ResponseEntity.ok(notificationService.updateNotification(id));
    }
}
