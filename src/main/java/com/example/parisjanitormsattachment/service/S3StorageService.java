package com.example.parisjanitormsattachment.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3StorageService {
    String uploadSupplierDoc(Long userId, String docType,String fileName, byte[] content);
    List<String> uploadPropertyImage(String  propertyId, List<MultipartFile> imgs) throws IOException;
    List<String> uploadPropertyDoc(String  propertyId, List<MultipartFile> files) throws IOException;
}