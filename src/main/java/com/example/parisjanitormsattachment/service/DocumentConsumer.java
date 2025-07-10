package com.example.parisjanitormsattachment.service;

import com.example.parisjanitormsattachment.dto.DocumentMsg;

public interface DocumentConsumer {

    public void handleIdentity(DocumentMsg msg);

    public void handleSiret(DocumentMsg msg);

    public void handleRib(DocumentMsg msg);

    public void saveFile(DocumentMsg msg);
}
