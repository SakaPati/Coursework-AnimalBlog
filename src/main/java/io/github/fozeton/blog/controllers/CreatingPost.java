package io.github.fozeton.blog.controllers;

import io.github.fozeton.blog.utils.RequestUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class CreatingPost {
    private final RequestUtil request = new RequestUtil();
    public Text title;
    public Label errorLabel;
    public Text heroTitle;
    public TextField titleInput;
    public StackPane imageContainer;
    public VBox uploadPlaceholder;
    public ImageView iconView;
    public Button selectImageBtn;
    public ImageView postPreview;
    public Text description;
    public TextField descriptionInput;
    public Button createPostBtn;

    public void selectedImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(selectImageBtn.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            postPreview.setImage(image);
            iconView.setVisible(false);
            selectImageBtn.setVisible(false);
        }
    }

    public void creatingPost(ActionEvent actionEvent) {
        if (titleInput.getText().isEmpty()) {
            errorLabel.setText("Установите заголовок");
            return;
        }
        if (postPreview.getImage() == null) {
            errorLabel.setText("Выберите изображение");
            return;
        }
        if (descriptionInput.getText().isEmpty()) {
            errorLabel.setText("Напишите описание");
            return;
        }

        try {
            byte[] keyByte = Files.readAllBytes(Paths.get("token.pub"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);

            Claims jwt = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(RequestUtil.getHeader()).getPayload();

            Map<String, String> params = Map.of(
                    "title", titleInput.getText(),
                    "content", descriptionInput.getText(),
                    "author", jwt.getSubject()
            );

            request.sendPostMultipart("http://localhost:8080/api/posts/creating", postPreview.getImage(), System.out::println, System.out::println, params);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
