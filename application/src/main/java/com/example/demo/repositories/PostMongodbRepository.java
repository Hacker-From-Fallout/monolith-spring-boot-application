package com.example.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PostMongodb;

@Repository
public interface PostMongodbRepository extends MongoRepository<PostMongodb, String> {

}
