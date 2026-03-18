package client.io.github.fozeton.blog.controllers;

import client.io.github.fozeton.blog.dto.CommentDto;
import client.io.github.fozeton.blog.dto.PostDto;
import client.io.github.fozeton.blog.utils.RequestUtil;
import client.io.github.fozeton.blog.utils.SwitcherScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;

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
        postLikeCount.setText(String.valueOf(postDto.getLikes()));
        postCommentCount.setText(String.valueOf(postDto.getComments().size()));
        updateLike(postDto.isLiked());
        updateComments(postDto.getComments());
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

    private void updateComments(List<CommentDto> comments) {
        commentsContainer.getChildren().clear();
        int count = Math.min(comments.size(), 5);
        if (!comments.isEmpty())
            for (int i = 0; i < count; i++) {
                CommentDto comment = comments.get(i);

                HBox commentRow = new HBox(10);

                ImageView avatar = new ImageView();
                avatar.setFitHeight(30);
                avatar.setFitWidth(30);
                avatar.setImage(request.getAvatarImage(comment.getUserName()));

                Text name = new Text(comment.getUserName() + ": ");
                Text content = new Text(comment.getContent());

                commentRow.getChildren().addAll(avatar, name, content);
                commentsContainer.getChildren().add(commentRow);
            }
    }

    public void openCommentsFeed(ActionEvent actionEvent) {
        SwitcherScene switcher = new SwitcherScene();

        PostDetail controller = switcher.openModal("PostDetail", "Комментарии");

        controller.setPostData(this.currentPost);
    }

    public void openUserProfile(javafx.scene.input.MouseEvent event) {
        if (currentPost == null) return;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setUser(currentPost.getAuthor());

            stage.setScene(new Scene(root));
            stage.setTitle("Профиль " + currentPost.getAuthor());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
