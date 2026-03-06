package io.github.fozeton.blog.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class Register {
    public Text registerTitle;
    public TextField registerLogin;
    public PasswordField registerPassword;
    public PasswordField repeatPassword;
    public CheckBox personalDataCheck;
    public Button registerButton;
    public Label errorLabel;

    public void register(ActionEvent event) {
        String login = registerLogin.getText();
        String pass = registerPassword.getText();
        String repeatPass = repeatPassword.getText();
        boolean assentDataCheck = personalDataCheck.isSelected();

        if(!login.isEmpty() && !pass.isEmpty() && !repeatPass.isEmpty()) {
            if(!pass.equals(repeatPass)) {
                errorLabel.setText("Напишите одинаковые пароли!");
                errorLabel.setVisible(true);
                return;
            }
            if(!assentDataCheck) {
                errorLabel.setText("Согласитесь с обработкой персональных данных!");
                errorLabel.setVisible(true);
                return;
            }
            errorLabel.setVisible(false);
            System.out.println("REGISTER!");
        } else {
            errorLabel.setText("Введите данные для регистрации!");
            errorLabel.setVisible(true);
        }
    }
}
