package com.example.parisjanitormsattachment.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface S3Service {

    Flux<String> getImages(String propertyId);
    Mono<String> uploadImages(String propertyId, MultipartFile image);
    Flux<String> uploadImages(String propertyId, List<MultipartFile> images);

}
