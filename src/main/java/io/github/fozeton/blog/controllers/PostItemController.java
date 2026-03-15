package io.github.fozeton.blog.controllers;

import io.github.fozeton.blog.dto.PostDto;
import io.github.fozeton.blog.utils.RequestUtil;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;

public class PostItemController {
    private final RequestUtil request = new RequestUtil();
    public ImageView userAvatar;
    public Text postAuthor;
    public Text postTitle;
    public ImageView postImage;
    public Text postContent;
    public Text postLikeCount;
    public ImageView postLike;
    public Text postCommentCount;
    public Button postComment;
    public VBox commentsContainer;
    private PostDto currentPost;

    public void setData(PostDto postDto) {
        this.currentPost = postDto;
        userAvatar.setImage(request.getAvatarImage(postDto.getAuthor()));
        postAuthor.setText(postDto.getAuthor());
        postTitle.setText(postDto.getTitle());
        postImage.setImage(request.getPostImage(postDto.getImageUrl()));
        postImage.setFitWidth(300);
        postImage.setFitHeight(300);
        postContent.setText(postDto.getContent());
        postLikeCount.setText(String.valueOf(postDto.getLikes().size()));
        postLikeCount.setText(String.valueOf(postDto.getComments().size()));
        updateLike(postDto.isLiked());
    }

    public void handleLikeClick() {
        if (currentPost == null) return;
        currentPost.setLiked(!currentPost.isLiked());
        updateLike(currentPost.isLiked());
        HttpResponse<String> newLikes = request.sendPost(URI.create("http://localhost:8080/api/posts/" + currentPost.getId() + "/" + request.getUserName() + "/like"));
        postLikeCount.setText(newLikes.body());
    }

    private void updateLike(boolean isLiked) {
        String path = isLiked ? "/static/icons/redLike.png" : "/static/icons/like.png";
        try {
            URL url = getClass().getResource(path);
            if (url == null) return;
            Image like = new Image(url.toURI().toString());
            postLike.setImage(like);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
