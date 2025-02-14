package com.example.parisjanitormsattachment.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    List<String> getImages(String propertyId);
    String uploadImages(String propertyId,MultipartFile image);
    List<String> uploadImages(String propertyId, List<MultipartFile> images);

}
