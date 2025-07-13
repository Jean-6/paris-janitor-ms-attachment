package com.example.parisjanitormsattachment.repository;


import com.example.parisjanitormsattachment.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends MongoRepository<Document,String> {

}
