package io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import io.github.fozeton.blog.dto.PostDto;
import io.github.fozeton.blog.dto.PostsDto;
import io.github.fozeton.blog.utils.RequestUtil;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.ImageView;

import java.net.URI;
import java.net.http.HttpResponse;

public class FeedController {
    private final Gson gson = new Gson();
    private final RequestUtil request = new RequestUtil();
    public ImageView profile;
    public ListView<PostDto> feedList;
    private int currentPage = 0;
    private boolean isLoading;
    private boolean responseEmpty;

    public void initialize() {
        feedList.setCellFactory(list -> new PostCell());
        loadNext();
        feedList.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            ScrollBar bar = (ScrollBar) feedList.lookup(".scroll-bar:vertical");
            bar.valueProperty().addListener((o, oldValue, newValue) -> {
                if (newValue.doubleValue() > .96 && !isLoading && !responseEmpty) loadNext();
            });
        });
    }

    private void loadNext() {
        isLoading = true;
        Thread.ofVirtual().start(() -> {
            HttpResponse<String> response = request.sendGet(URI.create("http://localhost:8080/api/posts/feed?page=" + currentPage));
            PostsDto postsDto = gson.fromJson(response.body(), PostsDto.class);

            Platform.runLater(() -> {
                for (PostDto post : postsDto.getContent()) feedList.getItems().add(post);

                currentPage++;
                responseEmpty = postsDto.getContent().isEmpty();
                isLoading = false;
            });
        });
    }
}
