package com.example.parisjanitormsattachment.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@Setter
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    // Getters et Setters
    private String accessKeyId;
    private String secretKey;
    private String region;
    private String s3BucketName;
}
