package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.service.FileStorageService;
import com.example.parisjanitormsattachment.service.ImageStorageService;
import com.example.parisjanitormsattachment.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class S3StorageServiceImpl implements S3StorageService {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3AsyncClient s3AsyncClient;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ImageStorageService imageStorageService;

    @Override
    public String uploadDocument(Long userId, String docType, String fileName, byte[] content) {

        String key = "suppliers/" + userId + "/" + docType.toLowerCase() + "/" +fileName;
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

}