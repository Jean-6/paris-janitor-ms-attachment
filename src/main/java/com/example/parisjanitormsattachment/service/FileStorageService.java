package com.example.parisjanitormsattachment.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileStorageService {
    Mono<String> saveFiles(String propertyId, FilePart filePart, byte[] content, String fileName, String contentType);
}
