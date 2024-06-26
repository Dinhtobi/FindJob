package com.pblintern.web.Controller;

import com.pblintern.web.Services.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @PostMapping
    public ResponseEntity<?> addFavourite(@RequestParam(required = true) int id){
        return ResponseEntity.ok(favouriteService.addFavourite(id));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFavourite(@RequestParam(required = true) int id){
        return ResponseEntity.ok(favouriteService.deleteFavourite(id));
    }
}
