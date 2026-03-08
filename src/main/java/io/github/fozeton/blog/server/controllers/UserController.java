package io.github.fozeton.blog.server.controllers;

import io.github.fozeton.blog.server.dto.UserDto;
import io.github.fozeton.blog.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<SuccessfulMessage> register(@Valid @RequestBody UserDto user) {
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessfulMessage("Account successfully created"));
    }

    record SuccessfulMessage(String message) {}
}