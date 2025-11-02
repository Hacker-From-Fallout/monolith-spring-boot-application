package com.example.demo.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PostElasticsearch;

@Repository
public interface PostElasticsearchRepository extends ElasticsearchRepository<PostElasticsearch, String> {

}
