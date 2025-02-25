package com.example.parisjanitormsattachment.controller;

import com.example.parisjanitormsattachment.exception.ResourceNotFoundException;
import com.example.parisjanitormsattachment.service.impl.S3ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;


@Slf4j
@Service
@RestController
@RequestMapping("/api/img")
public class ImageController {

    @Autowired
    private S3ServiceImpl imageService;

    @PostMapping(value = "/upload/{propId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("files") MultipartFile[] files, @PathVariable String propId) {
        StringBuilder result = new StringBuilder();
        for(MultipartFile file : files){
            try{
                if(propId.isBlank()) return new ResponseEntity<>("Property ID is invalid", HttpStatus.BAD_REQUEST);
                String filename = imageService.uploadImages(propId,file);
                result.append(filename).append("\n");
            }catch (RuntimeException ex){
                return new ResponseEntity<>("Failed to upload files "+ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(result.toString(),HttpStatus.OK);
    }

    @GetMapping(value ="/download/{propId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> download(@PathVariable String propId) {
        List<String> fileUrls;
        try{
            if(propId.isBlank()) return new ResponseEntity<>("Property ID is invalid", HttpStatus.BAD_REQUEST);
            fileUrls = imageService.getImages(propId);
            if(fileUrls.isEmpty()) return new ResponseEntity<>("No image found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(fileUrls, HttpStatus.OK);
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>("Failed to download files "+ ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
