package client.io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import client.io.github.fozeton.blog.dto.CommentDto;
import client.io.github.fozeton.blog.dto.CommentResponse;
import client.io.github.fozeton.blog.dto.PostDto;
import client.io.github.fozeton.blog.utils.RequestUtil;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class PostDetail {
    private final Gson gson = new Gson();
    private final RequestUtil request = new RequestUtil();
    public ImageView userAvatar;
    public Text userName;
    public Text modalTitle;
    public ImageView modalImage;
    public Text modalContent;
    public VBox commentsContent;
    public Button loadMoreBtn;
    public TextField commentInput;
    private PostDto currentPost;
    private int currentPage = 0;

    public void setPostData(PostDto post) {
        this.currentPost = post;

        userAvatar.setImage(request.getAvatarImage(post.getAuthor()));
        userName.setText(post.getAuthor());
        modalTitle.setText(post.getTitle());
        modalContent.setText(post.getContent());
        modalImage.setImage(request.getPostImage(post.getImageUrl()));
        modalImage.setFitWidth(450);

        commentsContent.getChildren().clear();
        loadNextComments();
    }

    public void loadNextComments() {
        try {
            URI uri = URI.create("http://localhost:8080/api/posts/" + currentPost.getId() + "/comments?page=" + currentPage);
            HttpResponse<String> response = request.sendGet(uri);

            if (response.statusCode() == 200) {
                List<CommentDto> newComments = gson.fromJson(
                        response.body(),
                        new TypeToken<List<CommentDto>>() {
                        }.getType()
                );

                if (newComments.isEmpty()) {
                    loadMoreBtn.setVisible(false);
                    return;
                }

                for (CommentDto comment : newComments) commentsContent.getChildren().add(createCommentRow(comment));

                currentPage++;
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAddComment() {
        String text = commentInput.getText().trim();
        if (text.isEmpty()) return;
        commentInput.clear();

        URI uri = URI.create("http://localhost:8080/api/posts/" + currentPost.getId() + "/comment");
        String json = gson.toJson(Map.of(
                "userName", request.getUserName(),
                "content", text
        ));

        HttpResponse<String> response = request.sendPost(uri, json);

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            CommentResponse responseData = gson.fromJson(response.body(), CommentResponse.class);

            if (responseData != null && responseData.getContent() != null && !responseData.getContent().isEmpty()) {
                CommentDto savedComment = responseData.getContent().getFirst();

                Platform.runLater(() -> commentsContent.getChildren().addFirst(createCommentRow(savedComment)));
            }
        }
    }

    private HBox createCommentRow(CommentDto dto) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 5;");

        ImageView avatar = new ImageView(request.getAvatarImage(dto.getUserName()));
        avatar.setFitHeight(30);
        avatar.setFitWidth(30);

        Text author = new Text(dto.getUserName() + ": ");
        author.setStyle("-fx-font-weight: bold;");

        Text content = new Text(dto.getContent());
        content.setWrappingWidth(350);

        row.getChildren().addAll(avatar, author, content);
        return row;
    }
}