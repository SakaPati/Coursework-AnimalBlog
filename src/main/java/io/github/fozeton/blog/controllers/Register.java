package io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import io.github.fozeton.blog.dto.ErrorMessage;
import io.github.fozeton.blog.dto.UserDto;
import io.github.fozeton.blog.utils.RequestUtil;
import io.github.fozeton.blog.utils.SwitcherScene;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpResponse;

public class Register {
    private final Gson gson = new Gson();
    private final RequestUtil request = new RequestUtil();
    private final SwitcherScene switcherScene = new SwitcherScene();
    public Text registerTitle;
    public TextField registerLogin;
    public PasswordField registerPassword;
    public PasswordField repeatPassword;
    public CheckBox personalDataCheck;
    public Button registerButton;
    public Label errorLabel;

    public void register(ActionEvent event) {
        String login = registerLogin.getText();
        String password = registerPassword.getText();
        String repeatPass = repeatPassword.getText();
        boolean assentDataCheck = personalDataCheck.isSelected();

        if (!login.isEmpty() && !password.isEmpty() && !repeatPass.isEmpty()) {
            if (!password.equals(repeatPass)) {
                errorLabel.setText("Напишите одинаковые пароли!");
                return;
            }
            if (!assentDataCheck) {
                errorLabel.setText("Согласитесь с обработкой персональных данных!");
                return;
            }
            UserDto userDto = new UserDto(login, password);
            HttpResponse<String> response = request.sendPost(URI.create("http://localhost:8080/api/users/register"), gson.toJson(userDto));
            if (response.statusCode() >= 400) {
                ErrorMessage message = gson.fromJson(response.body(), ErrorMessage.class);
                errorLabel.setText(message.getError());
                return;
            }
            errorLabel.setVisible(false);
            switcherScene.switchScene(event, "login", "AnimalBlog: Login");
        } else errorLabel.setText("Введите данные для регистрации!");

    }


}
