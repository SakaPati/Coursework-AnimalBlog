package io.github.fozeton.blog.server.controllers;

import io.github.fozeton.blog.server.dto.PostDto;
import io.github.fozeton.blog.server.entity.Post;
import io.github.fozeton.blog.server.service.JwtService;
import io.github.fozeton.blog.server.service.PostService;
import io.github.fozeton.blog.server.utils.SuccessfulMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;

    public PostController(PostService postService, JwtService jwtService) {
        this.postService = postService;
        this.jwtService = jwtService;
    }

    @PostMapping("/creating")
    private ResponseEntity<SuccessfulMessage> creating(@Valid @ModelAttribute PostDto post) {

        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Post created successfully"));
    }

    @GetMapping("/feed")
    public ResponseEntity<SuccessfulMessage> feed(@RequestParam(name = "page") int page) {
        List<Post> posts = postService.getPostsByPage(page);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("successfully", posts));
    }

    @GetMapping("/images/{type}/{path}")
    public ResponseEntity<byte[]> images(@PathVariable String path, @PathVariable String type) {
        return postService.getImage(path, type);
    }
}
