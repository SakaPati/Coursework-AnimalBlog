package io.github.fozeton.blog.controllers;

import com.google.gson.Gson;
import io.github.fozeton.blog.dto.ErrorMessage;
import io.github.fozeton.blog.dto.User;
import io.github.fozeton.blog.utils.Requests;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpResponse;

public class Register {
    private final Gson gson = new Gson();
    private final Requests requests = new Requests();
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
                errorLabel.setVisible(true);
                return;
            }
            if (!assentDataCheck) {
                errorLabel.setText("Согласитесь с обработкой персональных данных!");
                errorLabel.setVisible(true);
                return;
            }
            errorLabel.setVisible(false);
            User user = new User(login, password);
            HttpResponse<String> response = requests.sendPost(URI.create("http://localhost:8080/api/users/register"), gson.toJson(user));
            if(response.statusCode() >= 400) {
                ErrorMessage message = gson.fromJson(response.body(), ErrorMessage.class);
                errorLabel.setText(message.error);
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Введите данные для регистрации!");
            errorLabel.setVisible(true);
        }
    }


}
