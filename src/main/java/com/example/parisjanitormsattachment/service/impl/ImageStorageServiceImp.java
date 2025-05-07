package com.example.parisjanitormsattachment.service.impl;

import com.example.parisjanitormsattachment.model.Image;
import com.example.parisjanitormsattachment.repository.ImageRepository;
import com.example.parisjanitormsattachment.service.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;


@Slf4j
@Service
public class ImageStorageServiceImp implements ImageStorageService {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private ImageRepository imageRepository;


    // Méthode utilitaire pour extraire l'extension du nom de fichier
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1) : "";
    }

    @Override
    public Mono<String> saveImages(String propertyId, FilePart filePart, byte[] content, String fileName, String contentType) {
        Image image = new Image();
        image.setPropertyId(propertyId);
        image.setFilename("https://" + bucketName + ".s3.amazonaws.com/" + fileName); // URL S3
        image.setFileType(getExtension(filePart.filename()));
        image.setContentType(contentType);
        image.setFileSize(content.length);
        image.setContent(content);
        image.setCreatedAt(LocalDateTime.now());

        log.debug("image : " + image.toString());

        return imageRepository.save(image)
                .map(Image::getId); // ou image.getFilename() si tu veux retourner l’URL
    }
}
