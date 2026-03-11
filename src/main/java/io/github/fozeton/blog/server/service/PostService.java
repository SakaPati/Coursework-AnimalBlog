package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.dto.PostDto;
import io.github.fozeton.blog.server.entity.Post;
import io.github.fozeton.blog.server.entity.User;
import io.github.fozeton.blog.server.exceptions.PostCreationException;
import io.github.fozeton.blog.server.repository.PostRepository;
import io.github.fozeton.blog.server.repository.UserRepository;
import io.github.fozeton.blog.server.utils.SavedFile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SavedFile savedFile = new SavedFile();

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void createPost(PostDto postDto) {
        try {
            String postImg = savedFile.saveFile(postDto.getImg(), "posts");
            System.out.println("postImg " + postImg);
            if (!postImg.isEmpty()) {
                Post post = new Post();
                post.setTitle(postDto.getTitle());
                post.setImageUrl(postImg);
                post.setContent(postDto.getContent());
                User user = userRepository.findByUserName(postDto.getAuthor());
                post.setUser(user);

                postRepository.save(post);
            } else throw new PostCreationException("Failed to create post");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
