package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "posts")
public class PostElasticsearch implements Post {

    final private String database = "Elasticsearch";

    @Id
    private String id;
    private String userId;
    private String text;
    private String imageUrl;
}
