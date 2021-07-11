package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.IInjector;
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

    /**
     * Sets fields using login info.
     *
     * @param loginInfo The login info.
     */
    public void setLoginInfo(LoginInfo loginInfo)
    {
        setIdentifier(loginInfo.getIdentifier());
        setUsername(loginInfo.getUsername());
        setPassword(loginInfo.getPassword());
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

    /**
     * Creates a new login info card.
     *
     * @param dependencyInjector Dependency injector.
     * @param loginInfo Login info.
     * @return The parent node of the info card.
     */
    public static Parent createNewCard(IInjector dependencyInjector, LoginInfo loginInfo)
    {
        FXMLLoader elementCardFxmlLoader = new FXMLLoader(ElementCardController.class.getClassLoader().getResource(
                "loginCard.fxml"));

        Parent elementCard = null;
        try
        {
            MainController mainController = dependencyInjector.resolve(MainController.class);

            elementCard = elementCardFxmlLoader.load();
            ElementCardController elementCardController = elementCardFxmlLoader.getController();

            elementCardController.setLoginInfo(loginInfo);
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
