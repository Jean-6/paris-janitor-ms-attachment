package com.example.parisjanitormsattachment.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;


public interface S3Service {

    Flux<String> getImages(String propertyId);
    Flux<String> uploads(String propertyId, String attachmentType, Flux<FilePart> fileParts);

}
