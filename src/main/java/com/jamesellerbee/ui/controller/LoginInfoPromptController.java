package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.IEncryptionEngine;
import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.injection.Injector;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginInfoPromptController
{
    /**
     * The title for a login info window.
     */
    public static String TITLE = "Create new stored login";

    private ILogger logger = new SimpleLogger(getClass().getName());

    private IInjector dependencyInjector;

    public LoginInfoPromptController()
    {
        dependencyInjector = Injector.getInstance();
    }

    @FXML
    private TextField username;
    @FXML
    private TextField identifier;
    @FXML
    private PasswordField hiddenPassword;

    public void onCreateClicked(MouseEvent mouseEvent)
    {
        ILoginInfoHandler loginInfoHandler = dependencyInjector.resolve(ILoginInfoHandler.class);
        if(loginInfoHandler != null)
        {
            LoginInfo newStoredLogin = new LoginInfo(identifier.getText(), username.getText(), hiddenPassword.getText());

            boolean success = loginInfoHandler.store(newStoredLogin);
            if(success)
            {
                clearFields();
            }
        }
        else
        {
            logger.error("Missing login info handler dependency.");
        }
    }

    public static Parent createLoginPrompt()
    {
        Parent root = null;
        try
        {
            FXMLLoader loginInfoPromptFxmlLoader = new FXMLLoader(LoginInfoPromptController.class.getClassLoader().getResource("loginInfoPrompt.fxml"));
            root = loginInfoPromptFxmlLoader.load();
            LoginInfoPromptController loginInfoPromptController = loginInfoPromptFxmlLoader.getController();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return root;
    }

    private void clearFields()
    {
        username.clear();
        identifier.clear();
        hiddenPassword.clear();
    }
}
