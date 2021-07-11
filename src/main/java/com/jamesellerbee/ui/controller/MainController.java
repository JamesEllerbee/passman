package com.jamesellerbee.ui.controller;

import com.jamesellerbee.ui.models.LoginInfo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainController
{
    private static final String VIEW = "View";
    private static final String HIDE = "Hide";
    private static final String HIDDEN_PASSWORD = "************";


    private String password;

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

    public void setSelected(LoginInfo loginInfo)
    {
        selectedIdentifier.setText(loginInfo.getIdentifier());
        selectedUsername.setText(loginInfo.getUsername());
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
            selectedPassword.setText(HIDDEN_PASSWORD);
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
    }

    private void setControlsEnabled(boolean enabled)
    {
        toggleViewPassword.setDisable(!enabled);
        closeButton.setDisable(!enabled);
    }
}
