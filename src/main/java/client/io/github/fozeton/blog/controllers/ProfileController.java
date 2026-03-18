package client.io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import client.io.github.fozeton.blog.dto.PostDto;
import client.io.github.fozeton.blog.utils.RequestUtil;
import client.io.github.fozeton.blog.utils.SwitcherScene;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ProfileController {
    private final SwitcherScene switcherScene = new SwitcherScene();
    private final RequestUtil request = new RequestUtil();
    private final Gson gson = new Gson();
    public ImageView userAvatar;
    public Text userName;
    public Text postCount;
    public TilePane postsGrid;

    private String currentProfileUser;

    public void setUser(String username) {
        this.currentProfileUser = username;
        this.userName.setText(username);
        this.userAvatar.setImage(request.getAvatarImage(username));
        loadUserPosts();
    }

    private void loadUserPosts() {
        URI uri = URI.create("http://localhost:8080/api/posts/user/" + currentProfileUser);
        HttpResponse<String> response = request.sendGet(uri);

        if (response.statusCode() == 200) {
            List<PostDto> posts = gson.fromJson(response.body(), new TypeToken<List<PostDto>>() {
            }.getType());
            postCount.setText("Постов: " + posts.size());

            postsGrid.getChildren().clear();
            for (PostDto post : posts) {
                ImageView preview = new ImageView(request.getPostImage(post.getImageUrl()));
                preview.setFitWidth(180);
                preview.setFitHeight(180);
                preview.setPreserveRatio(false);
                preview.setCursor(Cursor.HAND);

                preview.setOnMouseClicked(e -> openPostDetail(post));

                postsGrid.getChildren().add(preview);
            }
        }
    }

    public void handleChangeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(userAvatar.getScene().getWindow());

        if (file != null) {
            Image newImg = new Image(file.toURI().toString());

            request.sendPostMultipart(
                    "http://localhost:8080/api/users/upload",
                    newImg,
                    success -> {
                        userAvatar.setImage(newImg);
                    },
                    error -> System.err.println("Error: " + error),
                    Map.of("userName", request.getUserName(), "type", "avatar")
            );
        }
    }

    public void goBack(ActionEvent event) {
        switcherScene.switchScene(event, "feed", "AnimalBlog: Post feed");
    }

    private void openPostDetail(PostDto post) {
        PostDetail controller = switcherScene.openModal("PostDetail", "Просмотр поста");
        controller.setPostData(post);
    }

    public void handleCreatePost(ActionEvent actionEvent) {
        switcherScene.switchScene(actionEvent, "creatingPost", "Создание поста");
    }
}