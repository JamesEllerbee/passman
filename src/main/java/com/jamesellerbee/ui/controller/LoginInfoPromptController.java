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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginInfoPromptController
{
    /**
     * The title for a login info window.
     */
    public static String TITLE_CREATE_NEW = "Create new stored login";

    public static String TITLE_EDIT_EXISTING = "Edit stored login";

    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final IInjector dependencyInjector;

    private Region visibilityIcon;
    private Region visibilityOffIcon;

    // region FXML Fields
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
    @FXML
    public Button viewPassword;
    // endregion

    // region Constructors
    
    /**
     * Initializes a new LoginInfoPromptController.
     */
    public LoginInfoPromptController()
    {
        dependencyInjector = Injector.getInstance();
    }
    
    // endregion

    //region Public Methods

    /**
     * Performs any loading steps after the controls are loaded.
     */
    public void load()
    {
        setupIconButtons();
        changeViewPasswordIcon(true);
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

    public void onViewMouseClicked(MouseEvent mouseEvent)
    {
        boolean currentlyViewing = password.isVisible();
        if(currentlyViewing)
        {
            String passwordValue = password.getText();
            password.setVisible(false);
            password.setDisable(true);

            hiddenPassword.setText(passwordValue);
            hiddenPassword.setDisable(false);
            hiddenPassword.setVisible(true);

            changeViewPasswordIcon(true);
        }
        else
        {
            String passwordValue = hiddenPassword.getText();
            hiddenPassword.setVisible(false);
            hiddenPassword.setDisable(true);

            password.setText(passwordValue);
            password.setDisable(false);
            password.setVisible(true);

            changeViewPasswordIcon(false);
        }

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

            loginInfoPromptController.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return root;
    }

    //endregion

    private void setupIconButtons()
    {
        setupViewPasswordIconButton();
    }
    
    private void setupViewPasswordIconButton()
    {
        // Remove text from button if any
        if (viewPassword.getText() != null)
        {
            viewPassword.setText(null);
        }

        if (viewPassword.getTooltip() == null)
        {
            Tooltip viewPasswordToolTip = new Tooltip();
            viewPasswordToolTip.setText("Toggle visibility of the login info");

            viewPassword.setTooltip(viewPasswordToolTip);
        }

        if (!viewPassword.getStyleClass().contains("icon-button"))
        {
            viewPassword.getStyleClass().add("icon-button");
        }

        if (visibilityIcon == null)
        {
            visibilityIcon = new Region();
            visibilityIcon.getStyleClass().add("visibility");
        }

        if (visibilityOffIcon == null)
        {
            visibilityOffIcon = new Region();
            visibilityOffIcon.getStyleClass().add("visibility-off");
        }
    }

    private void changeViewPasswordIcon(boolean visibility)
    {
        setupViewPasswordIconButton();

        if (visibility)
        {
            viewPassword.setGraphic(visibilityIcon);
        }
        else
        {
            viewPassword.setGraphic(visibilityOffIcon);
        }
    }

    private void clearFields()
    {
        username.clear();
        identifier.clear();
        hiddenPassword.clear();
    }
}
