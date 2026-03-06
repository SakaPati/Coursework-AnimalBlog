package io.github.fozeton.blog.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    public Text loginTitle;
    public TextField loginField;
    public PasswordField loginPassword;
    public Button loginButton;
    public Label errorLabel;

    public void login(ActionEvent event) {
        String login = loginField.getText();
        String password = loginPassword.getText();
        if (!login.isEmpty() && !password.isEmpty()) {
            System.out.println("ABOBA");
        } else errorLabel.setVisible(true);
    }

    public void switchToRegister(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/io/github/fozeton/blog/register.fxml"));
        VBox vBox = loader.load();
        Scene register = new Scene(vBox);

        stage.setTitle("AnimalBlog: Register");
        stage.setScene(register);
        stage.show();
    }
}
