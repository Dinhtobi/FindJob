package com.pblintern.web.Services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    String storeFile(MultipartFile file, String phone, int i);

    Resource loadFileAsResource(String fileName);

    String createImgUrl(MultipartFile file);

    String createRootImgUrl(MultipartFile file, String phone, int i);

}
