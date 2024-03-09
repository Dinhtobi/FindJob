package com.pblintern.web.Controller;

import com.opencsv.exceptions.CsvException;
import com.pblintern.web.Services.Impl.IFileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileController {

    @Autowired
    private IFileStorageService fileStorageService;

    @GetMapping("/downloadFile/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable("filename") String fileName, HttpServletRequest request){
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(IOException e){
            log.info("Couldn't determine the file type!");
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/CSV")
    public ResponseEntity<?> addCSV(@RequestParam(value = "csv", required = false)MultipartFile file,
                                    @RequestParam(value = "type", required = false) String type) throws IOException, CsvException {
        return ResponseEntity.ok(fileStorageService.importFileCSV(file,type));
    }
}
