package io.github.fozeton.blog.server.controllers;

import io.github.fozeton.blog.server.dto.UploadFileDto;
import io.github.fozeton.blog.server.dto.UserDto;
import io.github.fozeton.blog.server.service.UserService;
import io.github.fozeton.blog.server.utils.ErrorMessage;
import io.github.fozeton.blog.server.utils.SuccessfulMessage;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<SuccessfulMessage> register(@Valid @RequestBody UserDto user) {
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessfulMessage("Registration is successful"));
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid @RequestBody UserDto user) {
        userService.login(user);
        try {
            byte[] keyByte = Files.readAllBytes(Paths.get("token.priv"));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);

            String jwt = Jwts.builder().subject(user.getUserName()).signWith(privateKey, Jwts.SIG.RS256).compact();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage(jwt));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Failed to create an account"));
        }
    }

    @PostMapping("/upload")
    private ResponseEntity<?> upload(@Valid @ModelAttribute UploadFileDto file) {
        if (file.getType().equals("avatar")) userService.setAvatar(file);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Unknown upload type"));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Avatar successfully changed"));
    }

    @GetMapping("/avatar/{userName}")
    private ResponseEntity<byte[]> avatarMe(@PathVariable String userName) {
        return userService.getAvatar(userName);
    }
}