package com.example.parisjanitormsattachment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.parisjanitormsattachment")
@EnableConfigurationProperties
public class ParisJanitorMsAttachmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParisJanitorMsAttachmentApplication.class, args);
    }

}
