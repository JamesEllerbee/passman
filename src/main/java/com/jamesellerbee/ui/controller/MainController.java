package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.injection.Injector;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import com.jamesellerbee.utilities.strings.StringUtility;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class MainController
{

    private ILogger logger = new SimpleLogger(getClass().getName());

    private static final String VIEW = "View";
    private static final String HIDE = "Hide";

    private IInjector dependencyInjector;
    private ILoginInfoHandler loginInfoHandler;
    private String password;
    private boolean creatingLogin = false;

    public MainController()
    {
        dependencyInjector = Injector.getInstance();
    }

    @FXML
    private Button edit;
    @FXML
    private Button delete;
    @FXML
    public Button closeButton;
    @FXML
    private Button toggleViewPassword;
    @FXML
    private Label selectedIdentifier;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label selectedUsername;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label selectedPassword;
    @FXML
    private VBox vboxContent;

    public void addContent(Parent root)
    {
        vboxContent.getChildren().addAll(root);
    }

    public void removeContent(Node root)
    {
        vboxContent.getChildren().remove(root);
    }

    public void setSelected(LoginInfo loginInfo)
    {
        selectedIdentifier.setText(loginInfo.getIdentifier());
        selectedUsername.setText(loginInfo.getUsername());
        selectedPassword.setText(StringUtility.getPasswordTemplate(loginInfo.getPassword()));
        password = loginInfo.getPassword();

        setControlsVisible(true);
        setControlsEnabled(true);

    }

    public void onCloseMouseClicked(MouseEvent mouseEvent)
    {
        selectedIdentifier.setText("");
        selectedUsername.setText("");
        password = "";

        setControlsVisible(false);
        setControlsEnabled(false);
    }

    public void OnViewToggleMouseClicked(MouseEvent mouseEvent)
    {
        if (isShowingPassword())
        {
            selectedPassword.setText(StringUtility.getPasswordTemplate(password));
            toggleViewPassword.setText(VIEW);
        }
        else
        {
            selectedPassword.setText(password);
            toggleViewPassword.setText(HIDE);
        }
    }

    private boolean isShowingPassword()
    {
        return selectedPassword.getText().equals(password);
    }

    private void setControlsVisible(boolean visible)
    {
        selectedIdentifier.setVisible(visible);
        usernameLabel.setVisible(visible);
        selectedUsername.setVisible(visible);
        passwordLabel.setVisible(visible);
        selectedPassword.setVisible(visible);
        toggleViewPassword.setVisible(visible);
        closeButton.setVisible(visible);
        edit.setVisible(visible);
        delete.setVisible(visible);
    }

    private void setControlsEnabled(boolean enabled)
    {
        toggleViewPassword.setDisable(!enabled);
        closeButton.setDisable(!enabled);
        edit.setDisable(!enabled);
        delete.setDisable(!enabled);
    }

    public void onAddNewMouseClicked(MouseEvent mouseEvent)
    {
        if (!creatingLogin)
        {
            Parent root = LoginInfoPromptController.createLoginPrompt();
            if (root != null)
            {
                Stage stage = new Stage();
                stage.setTitle(LoginInfoPromptController.TITLE);
                stage.setScene(new Scene(root, 600, 400));
                stage.setOnCloseRequest(event -> setCreatingLogin(false));
                stage.show();

                setCreatingLogin(true);
            }
        }
        else
        {
            logger.warn("Already creating a new stored login.");
        }
    }

    public void setCreatingLogin(boolean creatingLogin)
    {
        this.creatingLogin = creatingLogin;
    }

    public void onEditMouseClicked(MouseEvent mouseEvent)
    {
    }

    public void onDeleteMouseClicked(MouseEvent mouseEvent)
    {
        ILoginInfoHandler loginInfoHandler = getLoginInfoHandler();

        if (loginInfoHandler != null)
        {
            // Gather login info
            LoginInfo storedLoginToDelete = new LoginInfo(selectedIdentifier.getText(), selectedUsername.getText(), password);

            boolean success = loginInfoHandler.remove(storedLoginToDelete);
            if (success)
            {
                // Find child that matches the id
                AtomicReference<Node> toDeleteAtomicReference = new AtomicReference<>();
                vboxContent.getChildren().forEach(toDeleteAtomicReference::set);

                if (toDeleteAtomicReference.get() != null)
                {
                    removeContent(toDeleteAtomicReference.get());

                    setControlsVisible(false);
                    setControlsEnabled(false);
                }
            }
        }
        else
        {
            logger.error("Missing login info handler dependency.");
        }
    }

    private ILoginInfoHandler getLoginInfoHandler()
    {
        if (loginInfoHandler == null)
        {
            loginInfoHandler = dependencyInjector.resolve(ILoginInfoHandler.class);
        }

        return loginInfoHandler;
    }
}
