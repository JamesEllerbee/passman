package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ElementCardController
{
    private final ILogger logger = new SimpleLogger(getClass().getName());


    private MainController mainController;

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

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

    public static Parent createNewCard(MainController mainController, LoginInfo loginInfo)
    {
        FXMLLoader elementCardFxmlLoader = new FXMLLoader(ElementCardController.class.getClassLoader().getResource(
                "loginCard.fxml"));

        Parent elementCard = null;
        try
        {
            elementCard = elementCardFxmlLoader.load();
            ElementCardController elementCardController = elementCardFxmlLoader.getController();
            elementCardController.setIdentifier(loginInfo.getIdentifier());
            elementCardController.setUsername(loginInfo.getUsername());
            elementCardController.setPassword(loginInfo.getPassword());
            elementCardController.setMainController(mainController);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return elementCard;
    }

    public void onMouseClickedLoginInfo(MouseEvent mouseEvent)
    {
        if (mainController != null)
        {
            mainController.setSelected(new LoginInfo(identifier.getText(), username.getText(), password));
        }
        else
        {
            logger.error("Main controller is not initialized.");
        }
    }
}
