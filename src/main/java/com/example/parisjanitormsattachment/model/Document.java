package com.example.parisjanitormsattachment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
@org.springframework.data.mongodb.core.mapping.Document
public class Document {
    @Id
    private String id;
    private Long userId;
    private String docType;
    private String s3Url;
    private String filename;
}
