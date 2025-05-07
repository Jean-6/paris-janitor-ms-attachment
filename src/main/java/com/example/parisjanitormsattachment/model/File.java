package com.example.parisjanitormsattachment.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
public class File {
    @Id
    private String id;
    private String propertyId;
    private String filename;
    private String fileType;
    private long fileSize;
    private String contentType;
    private byte[] content;
    private String filePath;
    @CreatedDate
    private LocalDateTime createdAt;
}
