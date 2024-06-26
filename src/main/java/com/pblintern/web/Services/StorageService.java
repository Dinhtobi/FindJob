package com.pblintern.web.Services;

import com.amazonaws.HttpMethod;
import com.opencsv.exceptions.CsvException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.UrlResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageService {

    BaseResponse deleteFile(String bucketName, String fileName);

    UrlResponse createPresignedGetUrl(String bucketName, String fileName);

    UrlResponse createPresignedPutUrl(String bucketName, String fileName);

    String importFileCSV(MultipartFile file, String type) throws IOException, CsvException;

    void importField(List<String[]> data);

    void importCompany(List<String[]> data);

    void importJob(List<String[]> data);

    void importSkill(List<String[]> data);

    String storeCSV(MultipartFile file);
}
