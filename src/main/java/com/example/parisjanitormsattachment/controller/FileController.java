package com.example.parisjanitormsattachment.controller;
import com.example.parisjanitormsattachment.service.impl.S3ServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RestController
@RequestMapping("/api/attachment")
@Tag(name = "Attachment API", description = "Gestion des documents")
public class FileController {

    @Autowired
    private S3ServiceImpl s3Service;

    @GetMapping(value ="/{propId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<String>> getImages(@PathVariable String propId) {
        Flux<String> stringFlux=s3Service.getImages(propId)
                .doOnNext(log::info).onErrorResume(error->{
                    log.info(error.getMessage());
                    return Flux.error(error);
                });
        return ResponseEntity.status(HttpStatus.OK)
                .body(stringFlux);
    }

    @PostMapping(value = "/upload/doc/{propId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<List<String>>> uploadDocuments(
            @PathVariable String propId,
            @RequestPart("documents") Flux<FilePart> documents) {

        return s3Service.uploads(propId,"documents", documents)
                .collectList()
                .map(urls -> ResponseEntity.status(HttpStatus.OK).body(urls))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of(e.getMessage()))));
    }
}