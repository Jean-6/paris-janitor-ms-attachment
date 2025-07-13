package com.example.parisjanitormsattachment.dto;

import java.io.Serializable;

public record DocumentMsg (
        Long userId,
        String docType,
        String s3Url,
        byte[] content,
        String filename
) implements Serializable{}



