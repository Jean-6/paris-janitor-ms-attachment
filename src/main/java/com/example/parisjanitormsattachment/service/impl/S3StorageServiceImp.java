package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.service.DocumentService;
import com.example.parisjanitormsattachment.service.ImageService;
import com.example.parisjanitormsattachment.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class S3StorageServiceImp implements S3StorageService {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private S3Client s3Client;

    @Autowired
    private ImageService imageService;
    @Autowired
    private DocumentService documentService;


    @Override
    public String uploadSupplierDoc(Long userId, String docType, String fileName, byte[] content) {

        String key = "supplier_docs/" + userId + "/" + docType.toLowerCase() ;
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/pdf")
                        .build(),

                RequestBody.fromBytes(content)
        );
        return "https://"+bucketName+".s3.amazonaws.com/"+key;
    }

    @Override
    public List<String> uploadPropertyImage(String propertyId, List<MultipartFile> imgs) throws IOException {

        List<String> keys = new ArrayList<>();

        for(MultipartFile img: imgs){
            String key= "property_images/" + propertyId + "_"+ img.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(img.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(img.getInputStream(), img.getSize()));

            String s3Url= "https://"+bucketName+".s3.amazonaws.com/"+key;
            String uploadkey= imageService.saveImage(propertyId,img,s3Url, img.getOriginalFilename());
            keys.add(uploadkey);

        }
        s3Client.close();
        return keys;
    }

    @Override
    public List<String> uploadPropertyDoc(String propertyId, List<MultipartFile> files) throws IOException {

        List<String> keys = new ArrayList<>();
        for(MultipartFile file: files){
            String key = "property_docs/" + propertyId + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(),file.getSize()));
            String s3Url= "https://"+bucketName+".s3.amazonaws.com/"+key;
            String uploadKey = documentService.saveDocument(propertyId,file,s3Url,file.getOriginalFilename());
            keys.add(uploadKey);
        }
        s3Client.close();
        return keys;
    }
}