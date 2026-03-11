package io.github.fozeton.blog.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitcherScene {
    public void switchScene(ActionEvent event, String sceneName, String title) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            sceneSetter(stage, sceneName, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchScene(Stage stage, String sceneName, String title) {
        try {
            sceneSetter(stage, sceneName, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sceneSetter(Stage stage, String sceneName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/" + sceneName + ".fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}