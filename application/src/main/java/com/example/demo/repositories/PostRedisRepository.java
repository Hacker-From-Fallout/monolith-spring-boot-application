package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PostRedis;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository {

    private final RedisTemplate<String, PostRedis> redisTemplate;
    private static final String POSTS_KEY = "posts";

    public void save(PostRedis postRedis) {
        redisTemplate.opsForHash().put(POSTS_KEY, postRedis.getId(), postRedis);
    }

    public Optional<PostRedis> findById(String id) {
        PostRedis postRedis = (PostRedis) redisTemplate.opsForHash().get(POSTS_KEY, id);
        return Optional.ofNullable(postRedis);
    }

    public List<PostRedis> findAll() {
        List<Object> allPosts = redisTemplate.opsForHash().values(POSTS_KEY);
        return allPosts.stream()
                        .map(post -> (PostRedis) post)  
                        .collect(Collectors.toList());
    }

    public void update(PostRedis postRedis) {
        if (redisTemplate.opsForHash().hasKey(POSTS_KEY, postRedis.getId())) {
            redisTemplate.opsForHash().put(POSTS_KEY, postRedis.getId(), postRedis);
        } 
    }

    public void deleteById(String id) {
        redisTemplate.opsForHash().delete(POSTS_KEY, id);
    }
}
