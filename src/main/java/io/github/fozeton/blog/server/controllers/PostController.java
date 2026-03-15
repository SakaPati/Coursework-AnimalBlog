package io.github.fozeton.blog.server.controllers;

import io.github.fozeton.blog.server.dto.PostRequestDto;
import io.github.fozeton.blog.server.dto.PostResponseDto;
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
    private ResponseEntity<SuccessfulMessage> creating(@Valid @ModelAttribute PostRequestDto post) {
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("Post created successfully"));
    }

    @GetMapping("/feed")
    public ResponseEntity<SuccessfulMessage> feed(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "search", required = false) String query, @RequestParam(value = "user") String user) {
        List<PostResponseDto> posts;
        if (query == null || query.isEmpty()) posts = postService.getPostsByPage(page, user);
        else posts = postService.getPostsBySort(page, query, user);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessfulMessage("successfully", posts));
    }

    @GetMapping("/images/{path}")
    public ResponseEntity<byte[]> images(@PathVariable String path) {
        return postService.getImage(path);
    }

    @PostMapping("/{postId}/{userName}/like")
    public ResponseEntity<Integer> like(@PathVariable String userName, @PathVariable long postId) {
        postService.setLike(userName, postId);
        int likesCount = postService.getLikes(postId);
        return ResponseEntity.ok(likesCount);
    }
}
