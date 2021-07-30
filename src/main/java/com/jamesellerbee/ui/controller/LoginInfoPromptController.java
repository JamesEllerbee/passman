package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.injection.Injector;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginInfoPromptController
{
    /**
     * The title for a login info window.
     */
    public static String TITLE_CREATE_NEW = "Create new stored login";

    public static String TITLE_EDIT_EXISTING = "Edit stored login";



    private ILogger logger = new SimpleLogger(getClass().getName());

    private IInjector dependencyInjector;

    @FXML
    private TextField username;
    @FXML
    private TextField identifier;
    @FXML
    private PasswordField hiddenPassword;
    @FXML
    private TextField password;
    @FXML
    private Button create;

    public LoginInfoPromptController()
    {
        dependencyInjector = Injector.getInstance();
    }

    public void setUsernameText(String usernameText)
    {
        username.setText(usernameText);
    }

    public void setIdentifierText(String identifierText)
    {
        identifier.setText(identifierText);
    }

    public void setHiddenPasswordText(String passwordText)
    {
        hiddenPassword.setText(passwordText);
    }

    public void setCreateButtonText(String text)
    {
        create.setText(text);
    }


    public void onCreateClicked(MouseEvent mouseEvent)
    {
        ILoginInfoHandler loginInfoHandler = dependencyInjector.resolve(ILoginInfoHandler.class);
        if (loginInfoHandler != null)
        {
            boolean isNew = !create.getText().equals("Save");

            LoginInfo newStoredLogin = new LoginInfo(identifier.getText(), username.getText(), hiddenPassword.getText());

            boolean success = loginInfoHandler.store(newStoredLogin, isNew);
            if (success)
            {
                clearFields();
            }
            else
            {
                // Error dialog maybe
            }

            if(!isNew)
            {
                Stage stage = (Stage) create.getScene().getWindow();
                stage.close();
            }
        }
        else
        {
            logger.error("Missing login info handler dependency.");
        }
    }

    public static Parent createLoginPrompt()
    {
        return createLoginPrompt(null);
    }

    public static Parent createLoginPrompt(LoginInfo loginInfo)
    {
        Parent root = null;
        try
        {
            FXMLLoader loginInfoPromptFxmlLoader = new FXMLLoader(LoginInfoPromptController.class.getClassLoader().getResource("loginInfoPrompt.fxml"));
            root = loginInfoPromptFxmlLoader.load();
            LoginInfoPromptController loginInfoPromptController = loginInfoPromptFxmlLoader.getController();

            if (loginInfo != null)
            {
                loginInfoPromptController.setUsernameText(loginInfo.getUsername());
                loginInfoPromptController.setIdentifierText(loginInfo.getIdentifier());
                loginInfoPromptController.setHiddenPasswordText(loginInfo.getPassword());
                loginInfoPromptController.setCreateButtonText("Save");
            }
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
