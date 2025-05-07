package com.example.parisjanitormsattachment.repository;

import com.example.parisjanitormsattachment.model.File;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends ReactiveMongoRepository<File, Long> {
}
