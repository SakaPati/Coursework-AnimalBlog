package server.io.github.fozeton.blog.controllers;

import server.io.github.fozeton.blog.dto.CommentRequestDto;
import server.io.github.fozeton.blog.dto.CommentResponseDto;
import server.io.github.fozeton.blog.dto.PostRequestDto;
import server.io.github.fozeton.blog.dto.PostResponseDto;
import server.io.github.fozeton.blog.service.JwtService;
import server.io.github.fozeton.blog.service.PostService;
import server.io.github.fozeton.blog.utils.SuccessfulMessage;
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

    @PostMapping("/{postId}/comment")
    public ResponseEntity<SuccessfulMessage> comment(@PathVariable int postId, @Valid @RequestBody CommentRequestDto comment) {
        postService.createComment(postId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessfulMessage("Comment created"));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable long postId,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        List<CommentResponseDto> comments = postService.getCommentsByPage(postId, page);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(@PathVariable String username) {
        List<PostResponseDto> posts = postService.getPostsByUser(username);
        return ResponseEntity.ok(posts);
    }
}
