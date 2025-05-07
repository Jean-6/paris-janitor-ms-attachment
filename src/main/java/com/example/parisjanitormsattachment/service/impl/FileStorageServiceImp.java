package com.example.parisjanitormsattachment.service.impl;
import com.example.parisjanitormsattachment.model.File;
import com.example.parisjanitormsattachment.repository.FileRepository;
import com.example.parisjanitormsattachment.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@Service
public class FileStorageServiceImp implements FileStorageService {

    @Value("${aws.s3-bucket-name}")
    private String bucketName;
    @Autowired
    private FileRepository fileRepository;

    // Méthode utilitaire pour récupérer l'extension du fichier
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1) : "";
    }

    @Override
    public Mono<String> saveFiles(String propertyId, FilePart filePart, byte[] content, String fileName, String contentType) {
        File file = new File();
        file.setPropertyId(propertyId);
        file.setFilename("https://" + bucketName + ".s3.amazonaws.com/" + fileName); // URL S3
        file.setFileType(getExtension(filePart.filename()));
        file.setContentType(contentType);
        file.setFileSize(content.length);
        file.setContent(content);
        file.setCreatedAt(LocalDateTime.now());

        log.debug("file : " + file.toString());

        return fileRepository.save(file)
                .map(File::getId); // ou image.getFilename() si tu veux retourner l’URL
    }
}
