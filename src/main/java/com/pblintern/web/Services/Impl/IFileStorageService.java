package com.pblintern.web.Services.Impl;

import com.pblintern.web.Configs.FileStorageProperties;
import com.pblintern.web.Exceptions.FileStorageException;
import com.pblintern.web.Services.FileStorageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
@Slf4j
public class IFileStorageService implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public IFileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadFIle()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored: " + e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path resource " + fileName);
            }
            String dateTimeNowString = LocalDateTime.now().toString().replaceAll("-" , "").replace(":","").replace(".","");
            fileName = dateTimeNowString + "_" + fileName.trim().replace(" " ,"");
            Path targetLocation =  this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch(Exception e){
            throw new FileStorageException("Sorry! Couldn't store file " + fileName + " Please try again! " + e );
        }
    }

    @Override
    public String storeFile(MultipartFile file, String phone, int i) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path resource " + fileName);
            }
            String fileExtension = getFileExtension(fileName);
            fileName = "BusinessLicense" +"_" + i +"_" + phone + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch(IOException e){
            throw new FileStorageException("Sorry! Couldn't store file " + fileName + " Please try again! " + e );
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileStorageException("Sorry! File" + fileName +" not found!");
            }
        }catch(MalformedURLException e){
                throw new FileStorageException("File " + fileName +" not found with error: " + e );
        }
    }

    @Override
    public String createImgUrl(MultipartFile file) {
        String fileName = this.storeFile(file);
        String imgUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api")
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        return imgUrl;
    }

    @Override
    public String createRootImgUrl(MultipartFile file, String phone, int i) {
        String fileName = this.storeFile(file,phone,i);
        String imgUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api")
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        return imgUrl;
    }

    private static String getFileExtension(String fileName){
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex !=  -1  && lastDotIndex < fileName.length() -1){
            return fileName.substring(lastDotIndex);
        }else{
            return "";
        }
    }
}
