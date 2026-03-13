package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.dto.PostDto;
import io.github.fozeton.blog.server.entity.Post;
import io.github.fozeton.blog.server.exceptions.PostCreationException;
import io.github.fozeton.blog.server.repository.PostRepository;
import io.github.fozeton.blog.server.utils.SavedFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final SavedFile savedFile = new SavedFile();

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void createPost(PostDto postDto) {
        try {
            String postImg = savedFile.saveFile(postDto.getImg(), "posts");
            if (!postImg.isEmpty()) {
                Post post = new Post();
                post.setTitle(postDto.getTitle());
                post.setImageUrl(postImg);
                post.setContent(postDto.getContent());
                post.setAuthor(postDto.getAuthor());

                postRepository.save(post);
            } else throw new PostCreationException("Failed to create post");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> getPostsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createAt").descending());
        return postRepository.findAll(pageable).getContent();
    }

    public ResponseEntity<byte[]> getImage(String path, String type) {
        try{
            Path imgPath = Paths.get("images/" + type + "/", path);
            if(!Files.exists(imgPath)) return ResponseEntity.notFound().build();
            byte[] imgByte = Files.readAllBytes(imgPath);
            String contentType = Files.probeContentType(imgPath);
            MediaType mediaType = MediaType.parseMediaType(contentType);

            return ResponseEntity.ok().contentType(mediaType).body(imgByte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
