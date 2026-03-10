package io.github.fozeton.blog.utils;

import io.github.fozeton.blog.dto.User;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;

public class Request {
    private final OkHttpClient client = new OkHttpClient();
    @Setter
    private static User user;

    public HttpResponse<String> sendPost(URI uri, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(request);
    }


    public void test(URI uri, File file) {
        String fileName = file.getName();
        String type = fileName.substring(fileName.lastIndexOf("."));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "posts")
                .addFormDataPart("userName", user.getUserName())
                .addFormDataPart("img", fileName, RequestBody.create(file, MediaType.parse("image/" + type)))
                .build();

        Re
    }
//    public class UploadFileDto {
//        @NotBlank(message = "Type cannot be empty or contain spaces")
//        private String type;
//
//        @NotBlank
//        private String userName;
//
//        private MultipartFile img;
//    }

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
