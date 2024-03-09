package com.pblintern.web.Configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageProperties {

    @Value("${file.upload-file}")
    private String uploadFile;

    @Value("${file.csv}")
    private String fileCSV;


    public String getFileCSV() {
        return fileCSV;
    }

    public void setFileCSV(String fileCSV) {
        this.fileCSV = fileCSV;
    }

    public String getUploadFIle(){
        return uploadFile;
    }

    public void setUploadFile(String uploadFile){
        this.uploadFile = uploadFile;
    }
}
