package com.pblintern.web.Services.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.pblintern.web.Configs.FileStorageProperties;
import com.pblintern.web.Entities.*;
import com.pblintern.web.Enums.CSVEnum;
import com.pblintern.web.Exceptions.BadRequestException;
import com.pblintern.web.Exceptions.FileStorageException;
import com.pblintern.web.Exceptions.NotFoundException;
import com.pblintern.web.Payload.Responses.BaseResponse;
import com.pblintern.web.Payload.Responses.UrlResponse;
import com.pblintern.web.Repositories.*;
import com.pblintern.web.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class IStorageService implements StorageService {


    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;


    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private UserRepository userRepository;

    private final Path fileCSVLocation;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    public IStorageService(FileStorageProperties fileStorageProperties) {
        this.fileCSVLocation = Paths.get(fileStorageProperties.getFileCSV()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileCSVLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored: " + e);
        }
    }

    @Override
    public BaseResponse deleteFile(String bucket, String fileName) {
        s3Client.deleteObject(bucket, fileName);
        return new BaseResponse<Boolean>(true, fileName + " removed");
    }

    @Override
    public UrlResponse createPresignedGetUrl(String bucketName, String fileName) {
        System.setProperty("aws.accessKeyId", accessKey);
        System.setProperty("aws.secretAccessKey", secretKey);
        try(S3Presigner presigner = S3Presigner.builder().region(Region.of(region)).credentialsProvider(SystemPropertyCredentialsProvider.create()).build()){
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(objectRequest)
                    .build();
            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);
            return new UrlResponse(presignedGetObjectRequest.url().toExternalForm().toString());
        }
    }

    @Override
    public UrlResponse createPresignedPutUrl(String bucketName, String fileName) {
        System.setProperty("aws.accessKeyId", accessKey);

        System.setProperty("aws.secretAccessKey", secretKey);
        try(S3Presigner presigner = S3Presigner.builder().region(Region.of(region)).credentialsProvider(SystemPropertyCredentialsProvider.create()).build()){
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();
            PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(presignRequest);
            return new UrlResponse(presignedPutObjectRequest.url().toExternalForm().toString());
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
            }else{
                importSkill(allData);
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
                Field field = new Field();
                field.setName(" " + cell + " ");
                fieldRepository.save(field);
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
                    continue;
                }
                LocalDate startDate = LocalDate.of(2024, 3, 1);
                LocalDate endDate = LocalDate.of(2024, 5, 1);

                Date randomDate = generateRandomDate(startDate, endDate);
                post.setCreateAt(randomDate);
                List<String> listField = Arrays.stream(row[4].split(",")).toList();

                Set<Field> set = new HashSet<>();

                listField.stream().forEach(f -> {
                    Optional<Field> fieldOfActivityOptional = fieldRepository.findByName(f);
                    if(fieldOfActivityOptional.isPresent()){
                        Field field = fieldOfActivityOptional.get();
                        set.add(field);
                    }
                });
                if(set.isEmpty()){
                    continue;
                }else{
                    post.setFields(set);
                }
//                post.setSalary(row[5]);
                post.setExperience(row[6]);
                post.setLevel(row[7]);
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy");
                try{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(simpleDateFormat.parse(row[8]));
                    calendar.add(Calendar.MONTH, 1);
                    post.setExpire(calendar.getTime());
                }catch(Exception e){
                    post.setExpire(null);
                }
                Recruiter recruiter = recruiterRepository.findById(2).orElseThrow(()-> new NotFoundException("Employer not found!"));
                post.setRecruiter(recruiter);
                post.setDescription(row[9]);
                post.setRequirement(row[10]);
                postRepository.save(post);
            }
            index++;
        }
    }

    @Override
    public void importSkill(List<String[]> data) {
        for(String[] row : data)
        {
            Skills skill = new Skills();
            skill.setName(row[0]);
            skillRepository.save(skill);
        }
    }

    public static Date convertToLocalDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant());
    }

    public static Date generateRandomDate(LocalDate startDate, LocalDate endDate) {
        long startDay = startDate.toEpochDay();
        long endDay = endDate.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startDay, endDay);

        return convertToLocalDateViaInstant(LocalDate.ofEpochDay(randomDay));
    }
}
