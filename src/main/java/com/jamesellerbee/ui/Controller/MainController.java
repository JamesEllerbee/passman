package com.jamesellerbee.ui.Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class MainController
{
    @FXML
    private VBox vboxContent;

    public void addContent(Node node)
    {
        vboxContent.getChildren().add(node);
    }
}
