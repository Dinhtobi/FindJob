package com.pblintern.web.Services.Impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.pblintern.web.Configs.FileStorageProperties;
import com.pblintern.web.Entities.Company;
import com.pblintern.web.Entities.Employer;
import com.pblintern.web.Entities.FieldOfActivity;
import com.pblintern.web.Entities.Post;
import com.pblintern.web.Enums.CSVEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.FileStorageException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Repositories.CompanyRepository;
import com.pblintern.web.Repositories.EmployeerRepository;
import com.pblintern.web.Repositories.FieldRepository;
import com.pblintern.web.Repositories.PostRepository;
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
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IFileStorageService implements FileStorageService {

    private final Path fileStorageLocation;

    private final Path fileCSVLocation;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeerRepository employeerRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    public IFileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadFIle()).toAbsolutePath().normalize();
        this.fileCSVLocation = Paths.get(fileStorageProperties.getFileCSV()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileCSVLocation);
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
    @Override
    public String storeCSV(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path resource " + fileName);
            }
            String dateTimeNowString = LocalDateTime.now().toString().replaceAll("-","").replace(":","").replace(".","");
            fileName = dateTimeNowString +"_" +fileName.trim().replace(" ","");
            Path targetLocation = this.fileCSVLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch(Exception e){
            throw new FileStorageException("Sorry! Couldn't store file " + fileName +" Please try again!");        }
    }

    @Override
    public String importFileCSV(MultipartFile file, String type) throws IOException, CsvException {
            try{
                String path = this.fileCSVLocation +"/" +storeCSV(file);
                FileReader fileReader = new FileReader(path);
                CSVReader csvReader = new CSVReader(fileReader);
                List<String[]> allData = csvReader.readAll();
                if(type.equals(CSVEnum.FIELD.toString())){
                    importField(allData);
                }else if(type.equals(CSVEnum.COMPANY.toString())){
                    importCompany(allData);
                }else if(type.equals(CSVEnum.JOB.toString())){
                    importJob(allData);
                }
            }catch(Exception e){
                throw new BadRequestException("Sorry! Couldn't import into DB");
            }
        return "OK";
    }

    @Override
    public void importField(List<String[]> data) {
        for(String[] row : data)
            for(String cell : row){
                FieldOfActivity fieldOfActivity = new FieldOfActivity();
                fieldOfActivity.setName(cell);
                fieldRepository.save(fieldOfActivity);
            }
    }

    @Override
    public void importCompany(List<String[]> data) {
        int index = 0;
        for(String[] row : data)
        {
            if(index > 0){
                Company company = new Company();
                company.setIdCompany(row[1]);
                company.setName(row[2]);
                company.setLocation(row[3]);
                company.setCompanySize(row[4]);
                company.setWebSite(row[5]);
                company.setCompanyType(row[6]);
                company.setDescription(row[7]);
                company.setLogo(row[8]);
                companyRepository.save(company);
            }
            index++;
        }
    }

    @Override
    public void importJob(List<String[]> data) {
        int index = 0;
        for(String[] row : data)
        {
            if(index > 0){
                Post post = new Post();
                post.setName(row[1]);
                Optional<Company> companyOptional = companyRepository.findByIdCompany(row[2]);
                if(companyOptional.isPresent()){
                    Company company = companyOptional.get();
                    post.setCompany(company);
                }else{
                    post.setCompany(null);
                }
                Optional<FieldOfActivity> fieldOfActivityOptional = fieldRepository.findByName(row[4]);
                if(fieldOfActivityOptional.isPresent()){
                    FieldOfActivity fieldOfActivity = fieldOfActivityOptional.get();
                    post.setFieldOfActivity(fieldOfActivity);
                }else{
                    post.setFieldOfActivity(null);
                }
                post.setSalary(row[5]);
                post.setExperience(row[6]);
                post.setLevel(row[7]);
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("DD/mm/yyyy");
                try{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(simpleDateFormat.parse(row[8]));
                    calendar.add(Calendar.MONTH, 1);
                    post.setExpire(calendar.getTime());
                }catch(Exception e){
                    post.setExpire(null);
                }
                post.setCreateAt(new Date());
                Employer employer = employeerRepository.findById(2).orElseThrow(()-> new NotFoundException("Employer not found!"));
                post.setEmployer(employer);
                post.setDescription(row[9]);
                post.setRequirements(row[10]);
                postRepository.save(post);
            }
            index++;
        }
    }
}

