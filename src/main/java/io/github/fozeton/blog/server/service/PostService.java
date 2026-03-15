package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.dto.PostRequestDto;
import io.github.fozeton.blog.server.dto.PostResponseDto;
import io.github.fozeton.blog.server.entity.Like;
import io.github.fozeton.blog.server.entity.Post;
import io.github.fozeton.blog.server.entity.User;
import io.github.fozeton.blog.server.exceptions.PostCreationException;
import io.github.fozeton.blog.server.exceptions.PostNotFoundException;
import io.github.fozeton.blog.server.exceptions.UserNotFoundException;
import io.github.fozeton.blog.server.repository.LikeRepository;
import io.github.fozeton.blog.server.repository.PostRepository;
import io.github.fozeton.blog.server.repository.UserRepository;
import io.github.fozeton.blog.server.utils.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FileUtils fileUtils = new FileUtils();

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public void createPost(PostRequestDto postRequestDto) {
        try {
            String postImg = fileUtils.saveFile(postRequestDto.getImg(), "posts");
            if (!postImg.isEmpty()) {
                Post post = new Post();
                post.setTitle(postRequestDto.getTitle());
                post.setImageUrl(postImg);
                post.setContent(postRequestDto.getContent());
                post.setAuthor(postRequestDto.getAuthor());

                postRepository.save(post);
            } else throw new PostCreationException("Failed to create post");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PostResponseDto> getPostsByPage(int page, String user) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createAt").descending());
        List<Post> posts = postRepository.findAll(pageable).getContent();
        return posts.stream().map(post -> convertToDto(post, user)).toList();
    }

    public List<PostResponseDto> getPostsBySort(int page, String query, String user) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createAt").descending());
        List<Post> posts = postRepository.findByTitleContainingIgnoreCase(query, pageable).getContent();
        return posts.stream().map(post -> convertToDto(post, user)).toList();
    }

    public ResponseEntity<byte[]> getImage(String path) {
        try {
            Path imgPath = Paths.get("images/posts/", path);
            return fileUtils.getResponseEntity(imgPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLike(String userName, long postNumber) {
        User user = userRepository.findByUserName(userName);
        Post post = postRepository.findPostById(postNumber);
        if (user == null) throw new UserNotFoundException("User not found");
        if (post == null) throw new PostNotFoundException("Post not found");
        boolean isLiked = likeRepository.existsLikeByUserAndPost(user.getUserName(), post);
        if (!isLiked) {
            Like like = new Like();
            like.setUser(user.getUserName());
            like.setPost(post);
            likeRepository.save(like);
        } else likeRepository.deleteByUserAndPost(user.getUserName(), post);
    }

    public int getLikes(long postId) {
        return likeRepository.countByPostId(postId);
    }

    private PostResponseDto convertToDto(Post post, String user) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setAuthor(post.getAuthor());
        dto.setImageUrl(post.getImageUrl());
        dto.setContent(post.getContent());
        dto.setLikes(post.getLikes());
        dto.setComments(post.getComments());
        dto.setCreateAt(post.getCreateAt());
        boolean isLiked = post.getLikes().stream().anyMatch(like -> like.getUser().equals(user));
        dto.setLiked(isLiked);

        return dto;
    }
}
