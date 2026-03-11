package io.github.fozeton.blog.server.controllers;

import io.github.fozeton.blog.server.dto.PostDto;
import io.github.fozeton.blog.server.dto.UploadFileDto;
import io.github.fozeton.blog.server.dto.UserDto;
import io.github.fozeton.blog.server.exceptions.ErrorMessage;
import io.github.fozeton.blog.server.service.JwtService;
import io.github.fozeton.blog.server.service.PostService;
import io.github.fozeton.blog.server.service.UserService;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final JwtService jwtService;

    public UserController(UserService userService, PostService postService, JwtService jwtService) {
        this.userService = userService;
        this.postService = postService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    private ResponseEntity<?> register(@Valid @RequestBody UserDto user) {
        userService.register(user);

        try {
            byte[] keyByte = Files.readAllBytes(Paths.get("token.priv"));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);

            String jwt = Jwts.builder().subject(user.getUserName()).signWith(privateKey, Jwts.SIG.RS256).compact();
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessfulMessage(jwt));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Failed to create an account"));
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    private ResponseEntity<SuccessfulMessage> login(@Valid @RequestBody UserDto user) {
        userService.login(user);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Login is successful"));
    }

    @PostMapping("/upload")
    private ResponseEntity<?> upload(@Valid @ModelAttribute UploadFileDto file) {
        if (file.getType().equals("avatar")) userService.setAvatar(file);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Unknown upload type"));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Avatar successfully changed"));
    }

    @PostMapping("/creating")
    private ResponseEntity<SuccessfulMessage> creating(@Valid @ModelAttribute PostDto post, @RequestHeader("Authorization") String token) {
        jwtService.checkToken(token);
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Post created successfully"));
    }

    record SuccessfulMessage(String message) { }
}