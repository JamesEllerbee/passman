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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainController
{
    private ILogger logger = new SimpleLogger(getClass().getName());

    private static final String VIEW = "View";
    private static final String HIDE = "Hide";

    private IInjector dependencyInjector;
    private ILoginInfoHandler loginInfoHandler;
    private String password;
    private boolean creatingOrEditingLogin = false;
    private Map<String, Parent> loginInfoMap;

    private Region visibilityIcon;
    private Region visibilityOffIcon;
    private Region editIcon;
    private Region deleteIcon;

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

    public MainController()
    {
        dependencyInjector = Injector.getInstance();
        loginInfoMap = new HashMap<>();
    }

    public void addContent(String id, Parent root)
    {
        vboxContent.getChildren().addAll(root);
        loginInfoMap.put(id ,root);
    }

    public void updateContent(String id, Parent newCard)
    {
        vboxContent.getChildren().remove(loginInfoMap.get(id));
        vboxContent.getChildren().addAll(newCard);
        loginInfoMap.put(id, newCard);
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

        setupIconButtons();
        changeToggleViewPasswordIcon(true);
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
            changeToggleViewPasswordIcon(true);
        }
        else
        {
            selectedPassword.setText(password);
            changeToggleViewPasswordIcon(false);
        }
    }

    private void setupIconButtons()
    {
        setupEditIconButton();
        setupDeleteIconButton();
    }

    private void setupDeleteIconButton()
    {
        if (delete.getText() != null)
        {
            delete.setText(null);
        }

        if (delete.getTooltip() == null)
        {
            Tooltip deleteToolTip = new Tooltip();
            deleteToolTip.setText("Delete selected login info");
            delete.setTooltip(deleteToolTip);
        }

        if (deleteIcon == null)
        {
            deleteIcon = new Region();
            deleteIcon.getStyleClass().add("delete");
        }

        if (!delete.getStyleClass().contains("icon-button"))
        {
            delete.getStyleClass().add("icon-button");
            delete.setGraphic(deleteIcon);
        }
    }

    private void setupEditIconButton()
    {
        // Remove text from edit button if any
        if (edit.getText() != null)
        {
            edit.setText(null);
        }

        if (edit.getTooltip() == null)
        {
            Tooltip editToolTip = new Tooltip();
            editToolTip.setText("Edit selected login info");
            edit.setTooltip(editToolTip);
        }

        if (editIcon == null)
        {
            editIcon = new Region();
            editIcon.getStyleClass().add("edit");
        }

        if (!edit.getStyleClass().contains("icon-button"))
        {
            edit.getStyleClass().add("icon-button");
            edit.setGraphic(editIcon);
        }
    }

    private void changeToggleViewPasswordIcon(boolean visibility)
    {
        // Remove text from button if any
        if (toggleViewPassword.getText() != null)
        {
            toggleViewPassword.setText(null);
        }

        if (toggleViewPassword.getTooltip() == null)
        {
            Tooltip toggleViewPasswordToolTip = new Tooltip();
            toggleViewPasswordToolTip.setText("Toggle visibility of the login info");

            toggleViewPassword.setTooltip(toggleViewPasswordToolTip);
        }

        if (!toggleViewPassword.getStyleClass().contains("icon-button"))
        {
            toggleViewPassword.getStyleClass().add("icon-button");
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

        if (visibility)
        {
            toggleViewPassword.setGraphic(visibilityIcon);
        }
        else
        {
            toggleViewPassword.setGraphic(visibilityOffIcon);
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
        if (!creatingOrEditingLogin)
        {
            Parent root = LoginInfoPromptController.createLoginPrompt();
            if (root != null)
            {
                Stage stage = new Stage();
                stage.setTitle(LoginInfoPromptController.TITLE_CREATE_NEW);
                stage.setScene(new Scene(root, 600, 400));
                stage.setOnCloseRequest(event -> setCreatingOrEditingLogin(false));
                stage.show();

                setCreatingOrEditingLogin(true);
            }
        }
        else
        {
            logger.warn("Already creating a new stored login.");
        }
    }

    public void setCreatingOrEditingLogin(boolean creatingOrEditingLogin)
    {
        this.creatingOrEditingLogin = creatingOrEditingLogin;
    }

    public void onEditMouseClicked(MouseEvent mouseEvent)
    {
        if (!creatingOrEditingLogin)
        {
            Parent root = LoginInfoPromptController.createLoginPrompt(new LoginInfo(selectedIdentifier.getText(), selectedUsername.getText(), password));
            if (root != null)
            {
                Stage stage = new Stage();
                stage.setTitle(LoginInfoPromptController.TITLE_EDIT_EXISTING);
                stage.setScene(new Scene(root, 600, 400));
                stage.setOnCloseRequest(event -> setCreatingOrEditingLogin(false));
                stage.setOnHidden(event -> setCreatingOrEditingLogin(false));
                stage.show();

                setCreatingOrEditingLogin(true);
            }
        }
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
