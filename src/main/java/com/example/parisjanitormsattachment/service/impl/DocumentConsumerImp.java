package com.example.parisjanitormsattachment.service.impl;


import com.example.parisjanitormsattachment.dto.DocumentMsg;
import com.example.parisjanitormsattachment.model.Document;
import com.example.parisjanitormsattachment.repository.DocumentRepo;
import com.example.parisjanitormsattachment.service.DocumentConsumer;
import com.example.parisjanitormsattachment.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocumentConsumerImp implements DocumentConsumer {

    @Autowired
    private S3StorageService s3StorageService;
    @Autowired
    private DocumentRepo documentRepo;


    @RabbitListener(queues = "docs.identity")
    @Override
    public void handleIdentity(DocumentMsg msg) {

        log.debug("Received Identity document for user {}", msg.userId());

        String s3Url = s3StorageService.uploadDocument(
                msg.userId(),
                msg.docType(),
                msg.filename(),
                msg.content()
        );

        saveFile(msg.userId(), msg.docType(), msg.filename(), s3Url);

    }

    @RabbitListener(queues = "docs.siret")
    @Override
    public void handleSiret(DocumentMsg msg) {

        log.debug("Received Siret document for user {}", msg.userId());

        String s3Url = s3StorageService.uploadDocument(
                msg.userId(),
                msg.docType(),
                msg.filename(),
                msg.content()
        );

        saveFile(msg.userId(), msg.docType(), msg.filename(), s3Url);
    }

    @RabbitListener(queues = "docs.rib")
    @Override
    public void handleRib(DocumentMsg msg) {

        log.debug("Received Rib document for user {}", msg.userId());

        String s3Url = s3StorageService.uploadDocument(
                msg.userId(),
                msg.docType(),
                msg.filename(),
                msg.content()
        );

        saveFile(msg.userId(), msg.docType(), msg.filename(), s3Url);
    }

    @Override
    public Document saveFile(Long userId, String docType,String filename,String url) {

        Document newDocument = new Document();
        newDocument.setUserId(userId);
        newDocument.setDocType(docType);
        newDocument.setFilename(filename);
        newDocument.setS3Url(url);
        return documentRepo.insert(newDocument);
    }
}
