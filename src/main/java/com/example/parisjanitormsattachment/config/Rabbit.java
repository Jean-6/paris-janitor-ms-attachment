package com.example.parisjanitormsattachment.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Rabbit {

    public static final String EXCHANGE ="docs.exchange";

    //1. Exchange : Message entry point
    @Bean
    TopicExchange docsExchange(){
        return new TopicExchange(EXCHANGE, true, false);
    }

    //2. Queues : Files where my messages are stocked
    @Bean
    Queue identityQueue(){
        return new Queue("docs.identity", true);
    }

    @Bean
    Queue siretQueue(){
        return new Queue("docs.siret", true);
    }

    @Bean
    Queue ribQueue(){
        return new Queue("docs.rib", true);
    }

    //3. Bindings: bind exchange with routing key
    @Bean
    Binding identityBind(TopicExchange ex, Queue identityQueue){
        return BindingBuilder.bind(identityQueue).to(ex).with("docs.identity");
    }

    @Bean
    Binding siretBind(TopicExchange ex, Queue siretQueue){
        return BindingBuilder.bind(siretQueue).to(ex).with("docs.siret");
    }

    @Bean
    Binding ribBind(TopicExchange ex, Queue ribQueue){
        return BindingBuilder.bind(ribQueue).to(ex).with("docs.rib");
    }

    @Bean
    Jackson2JsonMessageConverter jackson(){ return new Jackson2JsonMessageConverter();}


}
