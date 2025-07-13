package com.example.parisjanitormsattachment.service;

import com.example.parisjanitormsattachment.dto.DocumentMsg;
import com.example.parisjanitormsattachment.model.Document;

public interface DocumentConsumer {

    void handleIdentity(DocumentMsg msg);

    void handleSiret(DocumentMsg msg);

    void handleRib(DocumentMsg msg);

    Document saveFile(Long userId, String docType, String filename, String url);
}
