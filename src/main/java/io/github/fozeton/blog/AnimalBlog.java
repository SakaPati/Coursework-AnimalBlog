package io.github.fozeton.blog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnimalBlog extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AnimalBlog: Login");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/io/github/fozeton/blog/login.fxml"));
        VBox vBox = loader.load();
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}