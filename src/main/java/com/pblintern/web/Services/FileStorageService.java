package com.pblintern.web.Services;

import com.opencsv.exceptions.CsvException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    String storeFile(MultipartFile file, String phone, int i);

    Resource loadFileAsResource(String fileName);

    String createImgUrl(MultipartFile file);

    String createRootImgUrl(MultipartFile file, String phone, int i);

    String storeCSV(MultipartFile file) ;

    String importFileCSV(MultipartFile file, String type) throws IOException, CsvException;

    void importField(List<String[]> data);

    void importCompany(List<String[]> data);

    void importJob(List<String[]> data);
}
