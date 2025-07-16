package com.example.parisjanitormsattachment.repository;

import com.example.parisjanitormsattachment.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;


@Repository
public interface ImageRepo extends MongoRepository<Image, String> {
    //Flux<Image> findByPropertyId(String propertyId);

}
