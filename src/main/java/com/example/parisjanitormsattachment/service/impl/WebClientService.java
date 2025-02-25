package com.example.parisjanitormsattachment.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    //@Autowired
    //private WebClient webClient;


    private final WebClient webClient;

    public WebClientService(WebClient webClient){
        this.webClient = WebClient.create("https://api.example.com");
    }

    public <T> Mono<T> get(String url, Class<T> responseType){
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> post(String url,Object requestBody, Class<T> responseType){
        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve().
                bodyToMono(responseType);
    }
}
