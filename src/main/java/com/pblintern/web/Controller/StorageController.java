package com.pblintern.web.Controller;

import com.amazonaws.HttpMethod;
import com.opencsv.exceptions.CsvException;
import com.pblintern.web.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService storageService;



    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestParam(value = "bucket") String bucket, @RequestParam(value = "filename") String fileName){
        return  ResponseEntity.ok(storageService.deleteFile(bucket,fileName));
    }

    @PostMapping
    public ResponseEntity<?> generateUrl(@RequestParam(value = "bucket") String bucket, @RequestParam(value = "filename") String fileName){
        return ResponseEntity.ok(storageService.createPresignedPutUrl(bucket,fileName));
    }

    @GetMapping
    public ResponseEntity<?> getUrl(@RequestParam(value = "bucket") String bucket, @RequestParam(value = "filename") String fileName){
        return ResponseEntity.ok(storageService.createPresignedGetUrl(bucket,fileName));
    }

    @PostMapping("/CSV")
    public ResponseEntity<?> addCSV(@RequestParam(value = "csv", required = false)MultipartFile file,
                                    @RequestParam(value = "type", required = false) String type) throws IOException, CsvException {
        return ResponseEntity.ok(storageService.importFileCSV(file,type));
    }
}
