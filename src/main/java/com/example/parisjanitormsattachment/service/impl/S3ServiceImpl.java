package com.example.parisjanitormsattachment.service.impl;

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
    public Mono<String> uploadImage(String propertyId, FilePart filePart ) {

        String fileName = propertyId + "/" + UUID.randomUUID() + "-" + filePart.filename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(filePart.headers().getContentType().toString())
                .build();

        return filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    //dataBuffer.release(); // Libérer le buffer
                    return bytes;
                })
                .reduce((bytes1, bytes2) -> {
                    byte[] combined = new byte[bytes1.length + bytes2.length];
                    System.arraycopy(bytes1, 0, combined, 0, bytes1.length);
                    System.arraycopy(bytes2, 0, combined, bytes1.length, bytes2.length);
                    return combined;
                })
                .flatMap(bytes -> Mono.fromFuture(
                        s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(bytes))
                ))
                .map(response -> "https://" + bucketName + ".s3.amazonaws.com/" + fileName)
                .onErrorResume(e -> Mono.error(new RuntimeException("Échec de l'upload vers S3 : " + e.getMessage())));
    }

    @Override
    public Flux<String> uploadImages(String propertyId, Flux<FilePart> fileParts) {
        return fileParts.flatMap( filePart -> {

            String fileName = propertyId + "/" + UUID.randomUUID() + "-" + filePart.filename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(filePart.headers().getContentType().toString())
                    .build();

            return filePart.content()
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        //dataBuffer.release(); // Libérer le buffer
                        return bytes;
                    })
                    .reduce((bytes1, bytes2) -> {
                        byte[] combined = new byte[bytes1.length + bytes2.length];
                        System.arraycopy(bytes1, 0, combined, 0, bytes1.length);
                        System.arraycopy(bytes2, 0, combined, bytes1.length, bytes2.length);
                        return combined;
                    })
                    .flatMap(bytes -> Mono.fromFuture(
                            s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(bytes))
                    ))
                    .map(response -> "https://" + bucketName + ".s3.amazonaws.com/" + fileName)
                    .onErrorResume(e -> Mono.error(new RuntimeException("Échec de l'upload vers S3 : " + e.getMessage())));

        });
    }

}


