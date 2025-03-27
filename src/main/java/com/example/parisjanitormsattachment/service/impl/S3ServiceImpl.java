package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;


@Slf4j
@Service
public class S3ServiceImpl implements S3Service {


    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private S3Client s3Client;

    @Override
    public Flux<String> getImages(String propertyId) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("properties/"+propertyId)
                .build();
        return Mono.fromCallable(()-> s3Client.listObjectsV2(request))
                .flatMapMany(response -> Flux.fromIterable(response.contents()))
                .map(s3Object -> "https://" + "s3.amazonaws.com/" + s3Object.key());

    }
    @Override
    public Mono<String> uploadImages(String propertyId, MultipartFile image) {
        return Mono.fromCallable(()->{

                String fileUrl = "";
                String filename = image.getOriginalFilename();
        try{
            String imgFile = "properties/" + propertyId + "/" + image.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imgFile)
                    .contentType(image.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,RequestBody.fromBytes(image.getBytes()));
            fileUrl = "https://" + bucketName + ".s3.amazonaws.com/"+ filename;
        }catch(Exception ex){
            log.error(ex.getMessage());
            throw new RuntimeException("",ex);
        }
        return fileUrl;
        });
    }


    @Override
    public Flux<String> uploadImages(String propertyId, List<MultipartFile> images) {
        return Flux.fromIterable(images)
                .flatMap(image -> {
                    return Mono.fromCallable(()->{
                        String filename = "";
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(filename)
                                .contentType(image.getContentType())
                                .build();
                        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
                        return "https://" + bucketName +".s3.amazonaws.com/"+ filename;
                    }).onErrorMap(e-> new RuntimeException(""));
                });
    }

}
