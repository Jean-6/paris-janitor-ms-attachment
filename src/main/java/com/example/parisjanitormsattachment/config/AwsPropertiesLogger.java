package com.example.parisjanitormsattachment.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AwsPropertiesLogger {

    private static final Logger logger = LoggerFactory.getLogger(AwsPropertiesLogger.class);
    private final AwsProperties awsProperties;

    public AwsPropertiesLogger(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @PostConstruct
    public void logProperties() {
        logger.info("AWS Access Key: {}", awsProperties.getAccessKeyId());
        logger.info("AWS Secret Key: {}", awsProperties.getSecretKey());
        logger.info("AWS Region: {}", awsProperties.getRegion());
    }
}
