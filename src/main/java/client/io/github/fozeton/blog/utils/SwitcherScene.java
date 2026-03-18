package client.io.github.fozeton.blog.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;

public class SwitcherScene {

    public void switchScene(ActionEvent event, String sceneName, String title) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScene(stage, sceneName, title);
    }

    public void switchScene(Stage stage, String sceneName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + sceneName + ".fxml"));
            Parent root = loader.load();

            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }

            stage.setTitle(title);

            stage.setMaximized(true);
            if (!stage.isShowing()) {
                stage.show();
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при переключении сцены: " + sceneName, e);
        }
    }

    public <T> T openModal(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlName + ".fxml"));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.setScene(new Scene(root));
            modalStage.initModality(Modality.APPLICATION_MODAL);

            modalStage.setMaximized(true);
            modalStage.show();

            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть модальное окно: " + fxmlName, e);
        }
    }
}