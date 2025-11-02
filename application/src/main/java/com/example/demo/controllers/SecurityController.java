package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/")
public class SecurityController {

    @Operation(summary = "Получить информацию о пользователе", 
                description = "Этот эндпоинт возвращает ID аутентифицированного пользователя, который сделал запрос.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена")
    })
    @GetMapping("/profile")
    public String userProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        List<?> authorities = (List<?>) authentication.getAuthorities();

        System.out.println("User id: " + id + " User authorities: " + authorities);

        return "User id: " + id + " User authorities: " + authorities;
    }

    @Operation(summary = "Тестовый эндпоинт для пользователей с ролью USER", 
                description = "Этот эндпоинт доступен только для пользователей с ролью USER.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь аутентифицирован")
    })
    @GetMapping("/user")
    public String user() {
        return "Аутентифицирован";
    }

    @Operation(summary = "Тестовый эндпоинт для пользователей с ролью ADMIN", 
                description = "Этот эндпоинт доступен только для пользователей с ролью ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь аутентифицирован")
    })
    @GetMapping("/admin")
    public String admin() {
        return "Аутентифицирован";
    }

    @Operation(summary = "Тестовый эндпоинт для пользователей с ролью ROOT", 
                description = "Этот эндпоинт доступен только для пользователей с ролью ROOT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь аутентифицирован")
    })
    @GetMapping("/root")
    public String root() {
        return "Аутентифицирован";
    }
}
