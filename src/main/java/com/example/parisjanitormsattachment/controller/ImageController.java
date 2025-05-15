package com.example.parisjanitormsattachment.controller;


import com.example.parisjanitormsattachment.model.Image;
import com.example.parisjanitormsattachment.repository.ImageRepository;
import com.example.parisjanitormsattachment.service.impl.S3ServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Images API", description = "Images Management")
public class ImageController {

    @Autowired
    private S3ServiceImpl s3Service;
    @Autowired
    private ImageRepository imageRepository;


    @Operation(summary = "Upload images for a property",
            description = "Upload multiple images for a given property ID and store them in S3.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image upload request",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/upload/img/{propId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<List<String>>> uploadImages(
            @PathVariable String propId,
            @RequestPart("pictures") Flux<FilePart> pictures) {

        return s3Service.uploads(propId,"pictures", pictures)
                .collectList()
                .map(urls -> ResponseEntity.status(HttpStatus.OK).body(urls))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of(e.getMessage()))));
    }

    @Operation(summary = "Retrieve all images",
            description = "Fetch all images available in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "images/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Image>> getImages() {

        Flux<Image> images = imageRepository.findAll()
                .doOnNext(image -> {
                    log.info("Image retrieved successfully");
                })
                .onErrorResume(error->{
                    log.error(error.getMessage());
                    return Flux.empty();
                });
        return ResponseEntity.ok().body(images);
    }

    @Operation(summary = "Retrieve images for a property",
            description = "Fetch multiple images for a given property ID from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "images/property/{propertyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Image>> getImages( @PathVariable String propertyId) {

        Flux<Image> images = imageRepository.findByPropertyId(propertyId)
                .doOnNext(image -> {
                    log.info("Image retrieved successfully");
                })
                .onErrorResume(error->{
                    log.error(error.getMessage());
                    return Flux.empty();
                });
        return ResponseEntity.ok().body(images);
    }
}
