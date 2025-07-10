package com.example.parisjanitormsattachment.service.impl;


import com.example.parisjanitormsattachment.dto.DocumentMsg;
import com.example.parisjanitormsattachment.service.DocumentConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocumentConsumerImp implements DocumentConsumer {


    @Override
    public void handleIdentity(DocumentMsg msg) {

    }

    @Override
    public void handleSiret(DocumentMsg msg) {

    }

    @Override
    public void handleRib(DocumentMsg msg) {

    }

    @Override
    public void saveFile(DocumentMsg msg) {

    }
}
