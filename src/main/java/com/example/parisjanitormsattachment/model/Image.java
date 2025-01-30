package com.example.parisjanitormsattachment.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "image")
public class Image {
    @Id
    private String id;
    private String propertyId;
    private String filename;
    private String fileType;
    private long fileSize;
    private String contentType;
    private byte[] content;
    @CreatedDate
    private LocalDateTime createdAt;
}
