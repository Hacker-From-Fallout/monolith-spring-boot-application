package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Post;
import com.example.demo.entities.PostElasticsearch;
import com.example.demo.entities.PostMongodb;
import com.example.demo.entities.PostPostgres;
import com.example.demo.entities.PostRedis;
import com.example.demo.services.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    final private PostService postService;

    @Operation(
        summary = "Получить все посты пользователя",
        description = "Этот метод возвращает список всех постов, созданных пользователем с заданным ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Список постов успешно возвращён",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(
        @Parameter(
            description = "Идентификатор пользователя, чьи посты нужно получить",
            required = true
        ) 
        @PathVariable String userId
    ) {
        return postService.getPostsByUserId(userId);
    }




    @Operation(
        summary = "Получить посты из PostgreSQL",
        description = "Этот метод возвращает все посты, хранящиеся в базе данных PostgreSQL.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешно возвращен список постов из PostgreSQL",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении постов из PostgreSQL",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/postgres")
    public List<PostPostgres> getPostgresPosts() {
        return postService.getPostgresPosts();
    }

    @Operation(
        summary = "Получить посты из MongoDB",
        description = "Этот метод возвращает все посты, хранящиеся в базе данных MongoDB.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешно возвращен список постов из MongoDB",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении постов из MongoDB",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/mongodb")
    public List<PostMongodb> getMongodbPosts() {
        return postService.getMongodbPosts();
    }

    @Operation(
        summary = "Получить посты из Redis",
        description = "Этот метод возвращает все посты, хранящиеся в базе данных Redis.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешно возвращен список постов из Redis",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении постов из Redis",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/redis")
    public List<PostRedis> getRedisPosts() {
        return postService.getRedisPosts();
    }

    @Operation(
        summary = "Получить посты из Elasticsearch",
        description = "Этот метод возвращает все посты, хранящиеся в базе данных Elasticsearch.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешно возвращен список постов из Elasticsearch",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении постов из Elasticsearch",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/elasticsearch")
    public List<PostElasticsearch> getElasticsearchPosts() {
        return postService.getElasticsearchPosts();
    }

    


    @Operation(
        summary = "Получить пост из PostgreSQL по ID",
        description = "Этот метод возвращает пост, найденный в базе данных PostgreSQL, по заданному ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно найден и возвращён",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении поста из PostgreSQL",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/postgres/{id}")
    public PostPostgres getPostFromPostgresById(
        @Parameter(description = "Идентификатор поста в PostgreSQL") @PathVariable String id
    ) {
        return postService.getPostFromPostgresById(id);
    }

    @Operation(
        summary = "Получить пост из MongoDB по ID",
        description = "Этот метод возвращает пост, найденный в базе данных MongoDB, по заданному ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно найден и возвращён",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении поста из MongoDB",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/mongodb/{id}")
    public PostMongodb getPostFromMongodbById(
        @Parameter(description = "Идентификатор поста в MongoDB") @PathVariable String id
    ) {
        return postService.getPostFromMongodbById(id);
    }

    @Operation(
        summary = "Получить пост из Redis по ID",
        description = "Этот метод возвращает пост, найденный в базе данных Redis, по заданному ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно найден и возвращён",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении поста из Redis",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/redis/{id}")
    public PostRedis getPostFromRedisById(
        @Parameter(description = "Идентификатор поста в Redis") @PathVariable String id
    ) {
        return postService.getPostFromRedisById(id);
    }

    @Operation(
        summary = "Получить пост из Elasticsearch по ID",
        description = "Этот метод возвращает пост, найденный в базе данных Elasticsearch, по заданному ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно найден и возвращён",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при получении поста из Elasticsearch",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/elasticsearch/{id}")
    public PostElasticsearch getPostFromElasticsearchById(
        @Parameter(description = "Идентификатор поста в Elasticsearch") @PathVariable String id
    ) {
        return postService.getPostFromElasticsearchById(id);
    }

    


    @Operation(
        summary = "Сохранить пост в PostgreSQL",
        description = "Этот метод сохраняет пост с текстом и изображением в базе данных PostgreSQL.",
        parameters = {
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Это пример текста поста."
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно сохранён в PostgreSQL"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при сохранении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping(value = "/postgres", consumes = "multipart/form-data")
    public void savePostToPostgres(
        @RequestParam @NotBlank(message = "Text must not be empty") String text, 
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.savePostToPostgres(text, imageFile);
    }

    @Operation(
        summary = "Сохранить пост в MongoDB",
        description = "Этот метод сохраняет пост с текстом и изображением в базе данных MongoDB.",
        parameters = {
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Это пример текста поста."
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно сохранён в MongoDB"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при сохранении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping(value = "/mongodb", consumes = "multipart/form-data")
    public void savePostToMongodb(
        @RequestParam @NotBlank(message = "Text must not be empty") String text, 
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.savePostToMongodb(text, imageFile);
    }


    @Operation(
        summary = "Сохранить пост в Redis",
        description = "Этот метод сохраняет пост с текстом и изображением в базе данных Redis.",
        parameters = {
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Это пример текста поста."
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно сохранён в Redis"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при сохранении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping(value = "/redis", consumes = "multipart/form-data")
    public void savePostToRedis(
        @RequestParam @NotBlank(message = "Text must not be empty") String text, 
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.savePostToRedis(text, imageFile);
    }

    @Operation(
        summary = "Сохранить пост в Elasticsearch",
        description = "Этот метод сохраняет пост с текстом и изображением в базе данных Elasticsearch.",
        parameters = {
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Это пример текста поста."
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно сохранён в Elasticsearch"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при сохранении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping(value = "/elasticsearch", consumes = "multipart/form-data")
    public void savePostToElasticsearch(
        @RequestParam @NotBlank(message = "Text must not be empty") String text, 
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.savePostToElasticsearch(text, imageFile);
    }


    


    @Operation(
        summary = "Обновить пост в PostgreSQL",
        description = "Этот метод обновляет пост с текстом и изображением в базе данных PostgreSQL по заданному ID.",
        parameters = {
            @Parameter(
                name = "id", 
                description = "Идентификатор поста в PostgreSQL", 
                required = true
            ),
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Обновлённый текст поста"
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно обновлён в PostgreSQL"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для обновления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при обновлении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PutMapping(value = "/postgres/{id}", consumes = "multipart/form-data")
    public void updatePostInPostgres(
        @PathVariable String id,
        @RequestParam @NotBlank(message = "Text must not be empty") String text,
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.updatePostInPostgres(id, text, imageFile);
    }

    @Operation(
        summary = "Обновить пост в MongoDB",
        description = "Этот метод обновляет пост с текстом и изображением в базе данных MongoDB по заданному ID.",
        parameters = {
            @Parameter(
                name = "id", 
                description = "Идентификатор поста в MongoDB", 
                required = true
            ),
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Обновлённый текст поста"
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно обновлён в MongoDB"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для обновления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при обновлении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PutMapping(value = "/mongodb/{id}", consumes = "multipart/form-data")
    public void updatePostInMongodb(
        @PathVariable String id,
        @RequestParam @NotBlank(message = "Text must not be empty") String text,
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.updatePostInMongodb(id, text, imageFile);
    }

    @Operation(
        summary = "Обновить пост в Redis",
        description = "Этот метод обновляет пост с текстом и изображением в базе данных Redis по заданному ID.",
        parameters = {
            @Parameter(
                name = "id", 
                description = "Идентификатор поста в Redis", 
                required = true
            ),
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Обновлённый текст поста"
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно обновлён в Redis"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для обновления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при обновлении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PutMapping(value = "/redis/{id}", consumes = "multipart/form-data")
    public void updatePostInRedis(
        @PathVariable String id,
        @RequestParam @NotBlank(message = "Text must not be empty") String text,
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.updatePostInRedis(id, text, imageFile);
    }

    @Operation(
        summary = "Обновить пост в Elasticsearch",
        description = "Этот метод обновляет пост с текстом и изображением в базе данных Elasticsearch по заданному ID.",
        parameters = {
            @Parameter(
                name = "id", 
                description = "Идентификатор поста в Elasticsearch", 
                required = true
            ),
            @Parameter(
                name = "text", 
                description = "Текст поста", 
                required = true, 
                example = "Обновлённый текст поста"
            ),
            @Parameter(
                name = "imageFile", 
                description = "Изображение для поста", 
                content = @Content(mediaType = "multipart/form-data")
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно обновлён в Elasticsearch"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неверные данные в запросе",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для обновления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при обновлении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PutMapping(value = "/elasticsearch/{id}", consumes = "multipart/form-data")
    public void updatePostInElasticsearch(
        @PathVariable String id,
        @RequestParam @NotBlank(message = "Text must not be empty") String text,
        @RequestParam @NotNull(message = "Image file must be provided") MultipartFile imageFile
    ) {
        postService.updatePostInElasticsearch(id, text, imageFile);
    }




    @Operation(
        summary = "Удалить пост из PostgreSQL",
        description = "Этот метод удаляет пост из базы данных PostgreSQL по заданному ID.",
        parameters = {
            @Parameter(
                name = "postId", 
                description = "Идентификатор поста в PostgreSQL", 
                required = true
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно удалён из PostgreSQL"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для удаления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден в PostgreSQL",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при удалении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @DeleteMapping("/postgres/{postId}")
    public ResponseEntity<String> deletePostFromPostgres(@PathVariable String postId) {
        postService.deletePostFromPostgresById(postId);
        return ResponseEntity.ok("Post with ID " + postId + " deleted from PostgreSQL.");
    }

    @Operation(
        summary = "Удалить пост из MongoDB",
        description = "Этот метод удаляет пост из базы данных MongoDB по заданному ID.",
        parameters = {
            @Parameter(
                name = "postId", 
                description = "Идентификатор поста в MongoDB", 
                required = true
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно удалён из MongoDB"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для удаления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден в MongoDB",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при удалении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @DeleteMapping("/mongodb/{postId}")
    public ResponseEntity<String> deletePostFromMongodb(@PathVariable String postId) {
        postService.deletePostFromMongodbById(postId);
        return ResponseEntity.ok("Post with ID " + postId + " deleted from MongoDB.");
    }

    @Operation(
        summary = "Удалить пост из Redis",
        description = "Этот метод удаляет пост из базы данных Redis по заданному ID.",
        parameters = {
            @Parameter(
                name = "postId", 
                description = "Идентификатор поста в Redis", 
                required = true
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно удалён из Redis"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для удаления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден в Redis",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при удалении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @DeleteMapping("/redis/{postId}")
    public ResponseEntity<String> deletePostFromRedis(@PathVariable String postId) {
        postService.deletePostFromRedisById(postId);
        return ResponseEntity.ok("Post with ID " + postId + " deleted from Redis.");
    }

    @Operation(
        summary = "Удалить пост из Elasticsearch",
        description = "Этот метод удаляет пост из базы данных Elasticsearch по заданному ID.",
        parameters = {
            @Parameter(
                name = "postId", 
                description = "Идентификатор поста в Elasticsearch", 
                required = true
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пост успешно удалён из Elasticsearch"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Нет прав для удаления поста (Access Denied)",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Пост с таким ID не найден в Elasticsearch",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Ошибка на сервере при удалении поста",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @DeleteMapping("/elasticsearch/{postId}")
    public ResponseEntity<String> deletePostFromElasticsearch(@PathVariable String postId) {
        postService.deletePostFromElasticsearchById(postId);
        return ResponseEntity.ok("Post with ID " + postId + " deleted from Elasticsearch.");
    }
}
