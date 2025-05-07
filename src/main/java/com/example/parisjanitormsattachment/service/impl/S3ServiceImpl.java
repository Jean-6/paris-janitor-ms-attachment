package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.service.FileStorageService;
import com.example.parisjanitormsattachment.service.ImageStorageService;
import com.example.parisjanitormsattachment.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3AsyncClient s3AsyncClient;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ImageStorageService imageStorageService;


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

    private byte[] combine(byte[] a, byte[] b) {
        byte[] combined = new byte[a.length + b.length];
        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);
        return combined;
    }

    @Override
    public Flux<String> uploads(String propertyId, String attachmentType, Flux<FilePart> fileParts) {
        return fileParts.flatMap(filePart -> {


            String fileName = propertyId + "/" + UUID.randomUUID() + "-" + filePart.filename();
            String contentType = filePart.headers().getContentType().toString();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            return filePart.content()
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        return bytes;
                    })
                    .reduce(this::combine)
                    .flatMap(bytes -> Mono.fromFuture(
                            s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(bytes))
                    ).then(
                            "pictures".equalsIgnoreCase(attachmentType)
                                    ? imageStorageService.saveImages(propertyId, filePart, bytes, fileName, contentType)
                                    : fileStorageService.saveFiles(propertyId, filePart, bytes, fileName, contentType)
                            )
                    );
        });
    }

}


