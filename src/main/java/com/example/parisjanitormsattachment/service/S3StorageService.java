package com.example.parisjanitormsattachment.service;


public interface S3StorageService {
    String uploadDocument(Long userId, String docType,String fileName, byte[] content);
}
