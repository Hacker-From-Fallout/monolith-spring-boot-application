package com.example.demo.entities;

import java.io.Serializable;

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
public class PostRedis implements Post, Serializable  {

    private static final long serialVersionUID = 1L;

    final private String database = "Redis";

    private String id;
    private String userId;
    private String text;
    private String imageUrl;
}
