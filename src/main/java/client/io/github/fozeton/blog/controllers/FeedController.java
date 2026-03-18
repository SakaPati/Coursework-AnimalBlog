package client.io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import client.io.github.fozeton.blog.dto.PostDto;
import client.io.github.fozeton.blog.dto.PostsDto;
import client.io.github.fozeton.blog.utils.RequestUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

public class FeedController {
    private final Gson gson = new Gson();
    private final RequestUtil request = new RequestUtil();
    public ImageView profile;
    public ListView<PostDto> feedList;
    public TextField searchInput;
    private int currentPage = 0;
    private boolean isLoading;
    private boolean responseEmpty;

    public void initialize() {
        profile.setImage(request.getAvatarImage(request.getUserName()));
        feedList.setCellFactory(list -> new PostCell());
        loadNext("http://localhost:8080/api/posts/feed?page=" + currentPage + "&user=" + request.getUserName());

        feedList.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            ScrollBar bar = (ScrollBar) feedList.lookup(".scroll-bar:vertical");
            bar.valueProperty().addListener((o, oldValue, newValue) -> {
                if (newValue.doubleValue() > .96 && !isLoading && !responseEmpty)
                    loadNext("http://localhost:8080/api/posts/feed?page=" + currentPage + "&user=" + request.getUserName());
            });
        });

    }

    public void openMyProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.setUser(request.getUserName());

            Stage stage = (Stage) feedList.getScene().getWindow();

            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }

            stage.setTitle("Мой профиль - " + request.getUserName());
            stage.setMaximized(true);

            if (!stage.isShowing()) {
                stage.show();
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при переходе в профиль", e);
        }
    }

    private void loadNext(String url) {
        isLoading = true;
        Thread.ofVirtual().start(() -> {
            HttpResponse<String> response = request.sendGet(URI.create(url));
            PostsDto postsDto = gson.fromJson(response.body(), PostsDto.class);
            if (postsDto.getContent() != null)
                Platform.runLater(() -> {
                    for (PostDto post : postsDto.getContent()) feedList.getItems().add(post);
                    currentPage++;
                    responseEmpty = postsDto.getContent().isEmpty();
                    isLoading = false;
                });
        });
    }

    public void search() {
        feedList.getItems().clear();
        currentPage = 0;
        String query = searchInput.getText();
        loadNext("http://localhost:8080/api/posts/feed?search=" + query + "&page=" + currentPage + "&user=" + request.getUserName());
    }
}
