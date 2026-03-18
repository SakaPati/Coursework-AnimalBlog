package server.io.github.fozeton.blog.service;

import server.io.github.fozeton.blog.dto.CommentRequestDto;
import server.io.github.fozeton.blog.dto.CommentResponseDto;
import server.io.github.fozeton.blog.dto.PostRequestDto;
import server.io.github.fozeton.blog.dto.PostResponseDto;
import server.io.github.fozeton.blog.entity.Comment;
import server.io.github.fozeton.blog.entity.Like;
import server.io.github.fozeton.blog.entity.Post;
import server.io.github.fozeton.blog.entity.User;
import server.io.github.fozeton.blog.exceptions.PostCreationException;
import server.io.github.fozeton.blog.exceptions.PostNotFoundException;
import server.io.github.fozeton.blog.exceptions.UserNotFoundException;
import server.io.github.fozeton.blog.repository.CommentRepository;
import server.io.github.fozeton.blog.repository.LikeRepository;
import server.io.github.fozeton.blog.repository.PostRepository;
import server.io.github.fozeton.blog.repository.UserRepository;
import server.io.github.fozeton.blog.utils.FileUtils;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FileUtils fileUtils = new FileUtils();

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
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

    public void createComment(long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findPostById(postId);
        User user = userRepository.findByUserName(commentRequestDto.getUserName());
        if (post == null) throw new PostNotFoundException("Post not found");
        if (user == null) throw new UserNotFoundException("User not found");

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(commentRequestDto.getContent());
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getCommentsByPage(long postId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());

        List<Comment> comments = commentRepository.findByPostId(postId, pageable).getContent();

        return comments.stream().map(comment -> {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setId(comment.getId());
            dto.setUserName(comment.getAuthor().getUserName());
            dto.setAvatarUrl(comment.getAuthor().getAvatarUrl());
            dto.setContent(comment.getContent());
            dto.setCreateAt(comment.getCreateAt());
            return dto;
        }).toList();
    }

    public List<PostResponseDto> getPostsByUser(String author) {
        List<Post> posts = postRepository.findByAuthor(author);
        return posts.stream().map(post -> convertToDto(post, author)).toList();
    }

    private PostResponseDto convertToDto(Post post, String user) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setAuthor(post.getAuthor());
        dto.setImageUrl(post.getImageUrl());
        dto.setContent(post.getContent());
        dto.setLikes(post.getLikes().size());

        dto.setComments(post.getComments().stream().map(comment -> {
            CommentResponseDto comResDto = new CommentResponseDto();
            comResDto.setId(comment.getId());
            comResDto.setAvatarUrl(comment.getAuthor().getAvatarUrl());
            comResDto.setUserName(comment.getAuthor().getUserName());
            comResDto.setContent(comment.getContent());
            comResDto.setCreateAt(comment.getCreateAt());
            return comResDto;
        }).toList());

        dto.setCreateAt(post.getCreateAt());
        boolean isLiked = post.getLikes().stream().anyMatch(like -> like.getUser().equals(user));
        dto.setLiked(isLiked);

        return dto;
    }
}
