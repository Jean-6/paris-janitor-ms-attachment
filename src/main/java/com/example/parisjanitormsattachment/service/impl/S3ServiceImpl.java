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
import java.util.stream.Collectors;


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
                .prefix("properties/"+propertyId)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(request);
        return  listObjectsV2Response.contents().stream()
                .map(s3Object -> "https://" + bucketName + "s3.amazonaws.com/" + s3Object.key())
                .collect(Collectors.toList());

    }

    @Override
    public String uploadImages(String propertyId, MultipartFile img) {
        String fileUrl = "";
        String filename = img.getOriginalFilename();
        try{
            String imgFile = "properties/" + propertyId + "/" + img.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imgFile)
                    .contentType(img.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,RequestBody.fromBytes(img.getBytes()));
            fileUrl = "https://" + bucketName + ".s3.amazonaws.com/"+ filename;
        }catch(Exception ex){
            log.error(ex.getMessage());
            throw new RuntimeException("",ex);
        }
        return fileUrl;
    }

    @Override
    public List<String> uploadImages(String propertyId, List<MultipartFile> imgs) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile img : imgs) {
            try {
                String fileName = "properties/" + propertyId + "/" + img.getOriginalFilename();
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(img.getContentType())
                        .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(img.getBytes()));
                String fileUrl = "https://" + bucketName +".s3.amazonaws.com/"+ fileName;
                fileUrls.add(fileUrl);
            }catch (Exception e){
                log.error(e.getMessage());
                throw new RuntimeException("Erreur lors de l'upload du fichier :" + img.getOriginalFilename(),e);
            }
        }
        return fileUrls;
    }

}
