package com.jamesellerbee.ui.Controller;

import com.jamesellerbee.models.LoginInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.io.IOException;

public class ElementCardController
{
    private String password;

    @FXML
    private TitledPane identifier;

    @FXML
    private Label username;

    public void setIdentifier(String identifierValue)
    {
        identifier.setText(identifierValue);
    }

    public void setUsername(String usernameValue)
    {
        username.setText(usernameValue);
    }


    public static Parent createNewCard(LoginInfo loginInfo)
    {
        FXMLLoader elementCardFxmlLoader = new FXMLLoader(ElementCardController.class.getClassLoader().getResource("loginCard.fxml"));

        Parent elementCard = null;
        try
        {
            elementCard = elementCardFxmlLoader.load();
            ElementCardController elementCardController = elementCardFxmlLoader.getController();
            elementCardController.setIdentifier(loginInfo.getIdentifier());
            elementCardController.setUsername(loginInfo.getUsername());

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return elementCard;
    }

}
