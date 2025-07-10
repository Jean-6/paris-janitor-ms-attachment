package com.example.parisjanitormsattachment.dto;

import java.io.Serializable;

public record DocumentMsg (
        Long userId,
        String docType,
        String fileName,
        String filePath
) implements Serializable{}



