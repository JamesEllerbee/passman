package com.jamesellerbee.ui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class ElementCardController
{
    @FXML
    private TitledPane identifier;


    @FXML
    private Label username;

    void setIdentifier(String identifierValue)
    {
        identifier.setText(identifierValue);
    }

    void setUsername(String usernameValue)
    {
        username.setText(usernameValue);
    }
}
