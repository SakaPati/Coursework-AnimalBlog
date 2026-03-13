package io.github.fozeton.blog.controllers;

import io.github.fozeton.blog.dto.PostDto;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.net.URL;

public class PostItemController {
    public Text postAuthor;
    public Text postTitle;
    public ImageView postImage;
    public Text postContent;
    public ImageView postLike;
    public Button postComment;
    public VBox commentsContainer;

    private boolean isLike;

    public void setData(PostDto postDto) {
        postAuthor.setText(postDto.getAuthor());
        postTitle.setText(postDto.getTitle());
        Image image = new Image("http://localhost:8080/api/posts/images/posts/" + postDto.getImageUrl(), true);
        postImage.setImage(image);
        postImage.setFitWidth(300);
        postImage.setFitHeight(300);
        postContent.setText(postDto.getContent());
    }

    public void handleLikeClick() {
        isLike = !isLike;

        if (isLike) {
            try {
                URL url = getClass().getResource("/static/icons/redLike.png");
                if (url == null) return;
                Image like = new Image(url.toURI().toString());
                postLike.setImage(like);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                URL url = getClass().getResource("/static/icons/like.png");
                if (url == null) return;
                Image like = new Image(url.toURI().toString());
                postLike.setImage(like);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
