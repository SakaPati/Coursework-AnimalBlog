package io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import io.github.fozeton.blog.dto.ErrorMessage;
import io.github.fozeton.blog.dto.User;
import io.github.fozeton.blog.utils.RequestUtil;
import io.github.fozeton.blog.utils.SwitcherScene;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpResponse;

public class Login {
    private final Gson gson = new Gson();
    private final RequestUtil request = new RequestUtil();
    private final SwitcherScene switcherScene = new SwitcherScene();
    public Text loginTitle;
    public TextField loginField;
    public PasswordField loginPassword;
    public Button loginButton;
    public Label errorLabel;

    public void login(ActionEvent event) {
        String login = loginField.getText();
        String password = loginPassword.getText();
        if (!login.isEmpty() && !password.isEmpty()) {
            User user = new User(login, password);
            HttpResponse<String> response = request.sendPost(URI.create("http://localhost:8080/api/users/login"), gson.toJson(user));
            if (response.statusCode() >= 400) {
                ErrorMessage message = gson.fromJson(response.body(), ErrorMessage.class);
                errorLabel.setText(message.getError());
                return;
            }
            switcherScene.switchScene(event, "creatingPost", "AnimalBlog: Post created");
        } else errorLabel.setText("Введите данные для входа");
    }

    public void switchToRegister(ActionEvent event) {
        switcherScene.switchScene(event, "register", "AnimalBlog: Register");
    }
}
