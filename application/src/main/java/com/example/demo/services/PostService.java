package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Post;
import com.example.demo.entities.PostElasticsearch;
import com.example.demo.entities.PostMongodb;
import com.example.demo.entities.PostPostgres;
import com.example.demo.entities.PostRedis;
import com.example.demo.exception.exceptions.AccessDeniedException;
import com.example.demo.exception.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.PostElasticsearchRepository;
import com.example.demo.repositories.PostMongodbRepository;
import com.example.demo.repositories.PostPostgresRepository;
import com.example.demo.repositories.PostRedisRepository;
import com.example.demo.security.IUserProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${minio.bucket}")
    private String BUCKET_NAME;

    final private PostPostgresRepository postPostgresRepository;
    final private PostMongodbRepository postMongodbRepository;
    final private PostRedisRepository postRedisRepository;
    final private PostElasticsearchRepository postElasticsearchRepository;
    final private MinioService minioService;

    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(String userId) {

        CompletableFuture<List<PostPostgres>> postgresPostsFuture = CompletableFuture.supplyAsync(() -> 
            postPostgresRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .collect(Collectors.toList())
        );
        
        CompletableFuture<List<PostMongodb>> mongodbPostsFuture = CompletableFuture.supplyAsync(() -> 
            postMongodbRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .collect(Collectors.toList())
        );
        
        CompletableFuture<List<PostRedis>> redisPostsFuture = CompletableFuture.supplyAsync(() -> 
            postRedisRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .collect(Collectors.toList())
        );
        
        CompletableFuture<List<PostElasticsearch>> elasticsearchPostsFuture = CompletableFuture.supplyAsync(() -> 
            StreamSupport.stream(postElasticsearchRepository.findAll().spliterator(), false)
                .filter(post -> post.getUserId().equals(userId))
                .collect(Collectors.toList())
        );

        return CompletableFuture.allOf(postgresPostsFuture, mongodbPostsFuture, redisPostsFuture, elasticsearchPostsFuture)
            .thenApply(v -> {
                try {
                    List<Post> allPosts = new ArrayList<>();
                    allPosts.addAll(postgresPostsFuture.get());
                    allPosts.addAll(mongodbPostsFuture.get());
                    allPosts.addAll(redisPostsFuture.get());
                    allPosts.addAll(elasticsearchPostsFuture.get());

                    return allPosts;
                } catch (Exception e) {
                    throw new RuntimeException("Error while fetching posts by userId", e);
                }
            }).join();
    }




    @Transactional(readOnly = true)
    public List<PostPostgres> getPostgresPosts() {
        return postPostgresRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PostMongodb> getMongodbPosts() {
        return postMongodbRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PostRedis> getRedisPosts() {
        return postRedisRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PostElasticsearch> getElasticsearchPosts() {
        Iterable<PostElasticsearch> elasticsearchPosts = postElasticsearchRepository.findAll();
        return StreamSupport.stream(elasticsearchPosts.spliterator(), false)
                .collect(Collectors.toList());
    }




    @Transactional
    public PostPostgres getPostFromPostgresById(String id) {
        return postPostgresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found in PostgreSQL with ID: " + id));
    }

    @Transactional
    public PostMongodb getPostFromMongodbById(String id) {
        return postMongodbRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found in MongoDB with ID: " + id));
    }

    @Transactional
    public PostRedis getPostFromRedisById(String id) {
        return postRedisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found in Redis with ID: " + id));
    }

    @Transactional
    public PostElasticsearch getPostFromElasticsearchById(String id) {
        return postElasticsearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found in Elasticsearch with ID: " + id));
    }




    @Transactional
    public void savePostToPostgres(String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        String imageUrl = uploadImageToMinio(imageFile);

        PostPostgres postPostgres = PostPostgres.builder()
                .userId(userId)
                .text(text)
                .imageUrl(imageUrl)
                .build();
        postPostgresRepository.save(postPostgres);
        log.info("Post saved to PostgreSQL.");
    }

    @Transactional
    public void savePostToMongodb(String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        String imageUrl = uploadImageToMinio(imageFile);

        PostMongodb postMongodb = PostMongodb.builder()
                .userId(userId)
                .text(text)
                .imageUrl(imageUrl)
                .build();
        postMongodbRepository.save(postMongodb);
        log.info("Post saved to MongoDB.");
    }

    @Transactional
    public void savePostToRedis(String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        String imageUrl = uploadImageToMinio(imageFile);

        PostRedis postRedis = PostRedis.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .text(text)
                .imageUrl(imageUrl)
                .build();
        postRedisRepository.save(postRedis);
        log.info("Post saved to Redis.");
    }

    @Transactional
    public void savePostToElasticsearch(String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        String imageUrl = uploadImageToMinio(imageFile);

        PostElasticsearch postElasticsearch = PostElasticsearch.builder()
                .userId(userId)
                .text(text)
                .imageUrl(imageUrl)
                .build();
        postElasticsearchRepository.save(postElasticsearch);
        log.info("Post saved to Elasticsearch.");
    }




    @Transactional
    public void updatePostInPostgres(String postId, String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostPostgres postPostgres = postPostgresRepository.findById(postId).orElse(null);

        if (postPostgres == null) {
            throw new ResourceNotFoundException("Post not found in PostgreSQL with ID: " + postId);
        }

        if (!postPostgres.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to update the post with ID " + postId);
        }

        String imageUrl = updateImageInMinio(imageFile, postPostgres.getImageUrl());

        postPostgres.setText(text);
        postPostgres.setImageUrl(imageUrl);

        postPostgresRepository.save(postPostgres);
        log.info("Post with ID {} updated in PostgreSQL.", postId);
    }

    @Transactional
    public void updatePostInMongodb(String postId, String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostMongodb postMongodb = postMongodbRepository.findById(postId).orElse(null);

        if (postMongodb == null) {
            throw new ResourceNotFoundException("Post not found in MongoDB with ID: " + postId);
        }

        if (!postMongodb.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to update the post with ID " + postId);
        }

        String imageUrl = updateImageInMinio(imageFile, postMongodb.getImageUrl());

        postMongodb.setText(text);
        postMongodb.setImageUrl(imageUrl);

        postMongodbRepository.save(postMongodb);
        log.info("Post with ID {} updated in MongoDB.", postId);
    }

    @Transactional
    public void updatePostInRedis(String postId, String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostRedis postRedis = postRedisRepository.findById(postId).orElse(null);

        if (postRedis == null) {
            throw new ResourceNotFoundException("Post not found in Redis with ID: " + postId);
        }

        if (!postRedis.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to update the post with ID " + postId);
        }

        String imageUrl = updateImageInMinio(imageFile, postRedis.getImageUrl());

        postRedis.setText(text);
        postRedis.setImageUrl(imageUrl);

        postRedisRepository.save(postRedis);
        log.info("Post with ID {} updated in Redis.", postId);
    }

    @Transactional
    public void updatePostInElasticsearch(String postId, String text, MultipartFile imageFile) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostElasticsearch postElasticsearch = postElasticsearchRepository.findById(postId).orElse(null);

        if (postElasticsearch == null) {
            throw new ResourceNotFoundException("Post not found in Elasticsearch with ID: " + postId);
        }

        if (!postElasticsearch.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to update the post with ID " + postId);
        }

        String imageUrl = updateImageInMinio(imageFile, postElasticsearch.getImageUrl());

        postElasticsearch.setText(text);
        postElasticsearch.setImageUrl(imageUrl);

        postElasticsearchRepository.save(postElasticsearch);
        log.info("Post with ID {} updated in Elasticsearch.", postId);
    }




    @Transactional
    public void deletePostFromPostgresById(String postId) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostPostgres postPostgres = postPostgresRepository.findById(postId).orElse(null);
        
        if (postPostgres == null) {
            throw new ResourceNotFoundException("Post not found in PostgreSQL with ID: " + postId);
        }

        if (!postPostgres.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to delete the post with ID " + postId);
        }

        deleteImageFromMinio(postPostgres.getImageUrl());
        postPostgresRepository.delete(postPostgres);
        log.info("Post with ID {} deleted from PostgreSQL.", postId);
    }

    @Transactional
    public void deletePostFromMongodbById(String postId) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostMongodb postMongodb = postMongodbRepository.findById(postId).orElse(null);
        
        if (postMongodb == null) {
            throw new ResourceNotFoundException("Post not found in MongoDB with ID: " + postId);
        }

        if (!postMongodb.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to delete the post with ID " + postId);
        }

        deleteImageFromMinio(postMongodb.getImageUrl());
        postMongodbRepository.delete(postMongodb);
        log.info("Post with ID {} deleted from MongoDB.", postId);
    }

    @Transactional
    public void deletePostFromRedisById(String postId) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostRedis postRedis = postRedisRepository.findById(postId).orElse(null);
        
        if (postRedis == null) {
            throw new ResourceNotFoundException("Post not found in Redis with ID: " + postId);
        }

        if (!postRedis.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to delete the post with ID " + postId);
        }

        deleteImageFromMinio(postRedis.getImageUrl());
        postRedisRepository.deleteById(postRedis.getId());
        log.info("Post with ID {} deleted from Redis.", postId);
    }

    @Transactional
    public void deletePostFromElasticsearchById(String postId) {
        IUserProfile principal = (IUserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        PostElasticsearch postElasticsearch = postElasticsearchRepository.findById(postId).orElse(null);
        
        if (postElasticsearch == null) {
            throw new ResourceNotFoundException("Post not found in Elasticsearch with ID: " + postId);
        }
        
        if (!postElasticsearch.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to delete the post with ID " + postId);
        }

        deleteImageFromMinio(postElasticsearch.getImageUrl());
        postElasticsearchRepository.delete(postElasticsearch);
        log.info("Post with ID {} deleted from Elasticsearch.", postId);
    }




    private String uploadImageToMinio(MultipartFile imageFile) {
        try {
            InputStream inputStream = imageFile.getInputStream();
            long contentLength = imageFile.getSize();
            String objectName = UUID.randomUUID().toString();

            return minioService.uploadImageToMinio(BUCKET_NAME, objectName, inputStream, contentLength);
        } catch (IOException e) {
            log.error("Failed to upload image to MinIO: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image to MinIO", e);
        }
    }

    private String updateImageInMinio(MultipartFile imageFile, String imageUrl) {
        try {
            InputStream inputStream = imageFile.getInputStream();
            long contentLength = imageFile.getSize();
            String objectName = getObjectNameByImageUrl(imageUrl);

            return minioService.updateImageInMinio(BUCKET_NAME, objectName, inputStream, contentLength);
        } catch (IOException e) {
            log.error("Failed to update image to MinIO: {}", e.getMessage());
            throw new RuntimeException("Failed to update image to MinIO", e);
        }
    }

    private void deleteImageFromMinio(String imageUrl) {
        String objectName = getObjectNameByImageUrl(imageUrl);

        minioService.deleteFile(BUCKET_NAME, objectName);
    }

    private String getObjectNameByImageUrl(String imageUrl) {
        String[] urlParts = imageUrl.split("/");
        String objectName = urlParts[urlParts.length - 1];

        return objectName;
    }
}
