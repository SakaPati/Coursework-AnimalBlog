package io.github.fozeton.blog.controllers;

import io.github.fozeton.blog.dto.PostDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;


public class PostCell extends ListCell<PostDto> {
    private final Parent root;
    private final PostItemController controller;

    public PostCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/postItem.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(PostDto post, boolean empty){
        super.updateItem(post, empty);
        if(empty || post == null) setGraphic(null);
        else {
            controller.setData(post);
            setGraphic(root);
        }
    }
}
