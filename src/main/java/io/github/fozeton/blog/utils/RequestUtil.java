package io.github.fozeton.blog.utils;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

public class RequestUtil {
    @Getter
    @Setter
    private static String header;
    private final OkHttpClient client = new OkHttpClient();

    public HttpResponse<String> sendPost(URI uri, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(request);
    }

    public void sendPostMultipart(String url, Image image, Consumer<String> onSuccess, Consumer<String> onError, Map<String, String> params) {
        String fileName = image.getUrl();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream img = new ByteArrayOutputStream();

        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            ImageIO.write(bufferedImage, type, img);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MultipartBody.Builder builderBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", fileName, RequestBody.create(img.toByteArray(), MediaType.parse("image/" + type)));
        params.forEach(builderBody::addFormDataPart);
        RequestBody requestBody = builderBody.build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + header)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> onError.accept(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        try {
                            onSuccess.accept(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else Platform.runLater(() -> {
                    try {
                        onError.accept(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    private HttpResponse<String> send(HttpRequest request) {
        try (
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(30))
                        .build()
        ) {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
