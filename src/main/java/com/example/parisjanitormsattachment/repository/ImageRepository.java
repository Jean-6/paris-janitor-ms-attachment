package com.example.parisjanitormsattachment.repository;

import com.example.parisjanitormsattachment.model.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    Flux<Image> findByPropertyId(String propertyId);

}
