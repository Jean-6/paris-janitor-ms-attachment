package com.example.parisjanitormsattachment.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("Microservice de Gestion des Pièces Jointes")
                .version("1.0")
                .description("Ce microservice permet de gérer l'upload, le stockage et la récupération de pièces jointes comme des images, des reçus et autres fichiers. " +
                        "Il expose des endpoints pour envoyer, récupérer, supprimer et gérer les métadonnées associées aux fichiers. Le service est basé sur une architecture WebFlux pour une gestion réactive des demandes.")
                        .contact(new Contact()
                                .name("Jean-Jaures Support Dev")
                                .email("oka.jeanjaures@gmail.com")
                                .url("https://github.com/Jean-6/paris-janitor-ms-attachment")
                        )
                        );
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("com.example.parisjanitormsattachment")  // Remplace par ton package
                .build();
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return OpenApi -> OpenApi.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperations().forEach(operation -> {
                operation.addTagsItem("Custom Tag");
            });
        });
    }
}
