package client.io.github.fozeton.blog;

import client.io.github.fozeton.blog.utils.SwitcherScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class AnimalBlog extends Application {
    private final SwitcherScene switcherScene = new SwitcherScene();

    @Override
    public void start(Stage stage) {
        switcherScene.switchScene(stage, "login", "AnimalBlog: Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}