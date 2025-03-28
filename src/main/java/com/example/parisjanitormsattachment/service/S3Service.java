package com.example.parisjanitormsattachment.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface S3Service {

    Flux<String> getImages(String propertyId);
    Mono<String> uploadImage(String propertyId, FilePart filePart);
    Flux<String> uploadImages(String propertyId, Flux<FilePart> fileParts);

}
