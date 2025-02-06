package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private S3Client s3Client;

    @Override
    public List<String> getImages(String propertyId) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(propertyId+"/images/")
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(request);
        List<String> fileNames = new ArrayList<>();
        listObjectsV2Response.contents().forEach(s3Object -> fileNames.add(s3Object.key()));
        return fileNames;
    }

    @Override
    public List<String> uploadImages(String propertyId, List<MultipartFile> images) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String fileName = "properties/" + propertyId + "/" + image.getOriginalFilename();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(image.getContentType())
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
                String fileUrl = "https://" + bucketName +".s3.amazonaws.com/"+ fileName;
                fileUrls.add(fileUrl);
            }catch (Exception e){
                log.error(e.getMessage());
                throw new RuntimeException("Erreur lors de l'upload du fichier :" + image.getOriginalFilename(),e);
            }
        }
        return fileUrls;
    }

}
