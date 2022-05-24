package com.jamesellerbee.ui.controller;

import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.injection.Injector;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import com.jamesellerbee.utilities.strings.StringUtility;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainController
{
    private final ILogger logger = new SimpleLogger(getClass().getName());

    // region Constants

    private static final String VIEW = "View";
    private static final String HIDE = "Hide";
    private static final String CLICK_TO_VIEW = "Select a card from the other panel to view login information.";
    private static final String ADD_NEW = "Click \"Add New\" to create new login information.";

    // endregion

    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    private final IInjector dependencyInjector;
    private ILoginInfoHandler loginInfoHandler;
    private String password;
    private boolean creatingOrEditingLogin = false;
    private final Map<String, Parent> loginInfoMap;
    private Thread hideInfoLabelTask;

    private Region visibilityIcon;
    private Region visibilityOffIcon;
    private Region editIcon;
    private Region deleteIcon;
    private Region copyIcon;

    // region FXML Fields

    @FXML
    private Button editLoginInfo;
    @FXML
    private Button deleteLoginInfo;
    @FXML
    public Button closeButton;
    @FXML
    private Button toggleViewPassword;
    @FXML
    private Button copyPassword;
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
    @FXML
    private Label viewInstruction;
    @FXML
    private Label infoLabel;


    // endregion

    // region Constructors

    /**
     * Initializes a new instance of the {@link MainController} class.
     */
    public MainController()
    {
        dependencyInjector = Injector.getInstance();
        loginInfoMap = new HashMap<>();
    }

    // endregion

    /**
     * Perform any loading steps after controls are added.
     */
    public void setUp()
    {
        setControlsVisible(false);
        setControlsEnabled(false);

        setupIconButtons();
        setViewInstructionText();

        infoLabel.setVisible(false);
    }

    /**
     * Adds the specified parent to the login info vbox content view.
     *
     * @param id   The id to associate with the root.
     * @param root The {@link Parent} object to add to the vbox content view.
     */
    public void addContent(String id, Parent root)
    {
        vboxContent.getChildren().addAll(root);
        loginInfoMap.put(id, root);
        setViewInstructionText();
    }

    public void updateContent(String id, Parent newCard)
    {
        vboxContent.getChildren().remove(loginInfoMap.get(id));
        vboxContent.getChildren().addAll(newCard);
        loginInfoMap.put(id, newCard);
    }

    public void removeContent(String id)
    {
        Parent loginCard = loginInfoMap.get(id);
        if (loginCard != null)
        {
            vboxContent.getChildren().remove(loginCard);
            loginInfoMap.remove(id);
            setViewInstructionText();
        }
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
        } else
        {
            selectedPassword.setText(password);
            changeToggleViewPasswordIcon(false);
        }
    }

    public void onAddNewMouseClicked(MouseEvent mouseEvent)
    {
        if (!creatingOrEditingLogin)
        {
            showLoginPrompt(LoginInfoPromptController.TITLE_EDIT_EXISTING, LoginInfoPromptController.createLoginPrompt());
            setCreatingOrEditingLogin(true);
        } else
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
            showLoginPrompt(LoginInfoPromptController.TITLE_EDIT_EXISTING, LoginInfoPromptController.createLoginPrompt(new LoginInfo(selectedIdentifier.getText(), selectedUsername.getText(), password)));
            setCreatingOrEditingLogin(true);
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
                removeContent(storedLoginToDelete.getIdentifier());
                setControlsVisible(false);
                setControlsEnabled(false);
            }
        } else
        {
            logger.error("Missing login info handler dependency.");
        }
    }

    public void onCopyButtonClicked(MouseEvent mouseEvent)
    {
        ClipboardContent content = new ClipboardContent();
        content.putString(password);
        clipboard.setContent(content);
        showAlert("Password copied to clipboard.");
    }

    // region Private Methods

    private void showAlert(String message)
    {
        infoLabel.setText(message);
        infoLabel.setVisible(true);

        // interrupt the hide info label task if it is running.
        if(hideInfoLabelTask != null &&  hideInfoLabelTask.isAlive())
        {
            hideInfoLabelTask.interrupt();
        }

        // create a new hide info label task.
        hideInfoLabelTask = new Thread(() ->
        {
            try
            {
                Thread.sleep(3000);
                infoLabel.setVisible(false);
            } catch (InterruptedException e)
            {
                logger.error("Alert thread interrupted.");
            }
        });

        // start the task
        hideInfoLabelTask.start();
    }

    private void setupIconButtons()
    {
        setupEditIconButton();
        setupDeleteIconButton();
        setupViewPasswordIconButton();
        setupCopyPasswordIconButton();
    }

    private void setupIconButton(Button button, Region region)
    {
        if (button.getText() != null)
        {
            button.setText(null);
        }

        if (!button.getStyleClass().contains("icon-button"))
        {
            button.getStyleClass().add("icon-button");
        }

        button.setGraphic(region);
    }

    private void setupTooltip(Button button, String text)
    {
        if(button.getTooltip() == null)
        {
            Tooltip copyToolTip = new Tooltip();
            copyToolTip.setText(text);
            button.setTooltip(copyToolTip);
        }
    }

    private void setupCopyPasswordIconButton()
    {
        if(copyIcon == null)
        {
            copyIcon = new Region();
            copyIcon.getStyleClass().add("copy");
        }

        setupIconButton(copyPassword, copyIcon);
        setupTooltip(copyPassword, "Copy password to clipboard");
    }

    private void setupDeleteIconButton()
    {
        if (deleteIcon == null)
        {
            deleteIcon = new Region();
            deleteIcon.getStyleClass().add("delete");
        }

        setupIconButton(deleteLoginInfo, deleteIcon);
        setupTooltip(deleteLoginInfo, "Delete selected login info");
    }

    private void setupEditIconButton()
    {
        if (editIcon == null)
        {
            editIcon = new Region();
            editIcon.getStyleClass().add("edit");
        }

        setupIconButton(editLoginInfo, editIcon);
        setupTooltip(editLoginInfo, "Edit selected login info");
    }

    private void setupViewPasswordIconButton()
    {
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

        setupIconButton(toggleViewPassword, visibilityOffIcon);
        setupTooltip(toggleViewPassword, "Toggle visibility of the login info");
    }

    private void changeToggleViewPasswordIcon(boolean visibility)
    {
        if (visibility)
        {
            toggleViewPassword.setGraphic(visibilityIcon);
        } else
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
        editLoginInfo.setVisible(visible);
        deleteLoginInfo.setVisible(visible);
        copyPassword.setVisible(visible);
        viewInstruction.setVisible(!visible);
    }

    private void setViewInstructionText()
    {
        String currentText = viewInstruction.getText();

        if (loginInfoMap.isEmpty() && !currentText.equals(ADD_NEW))
        {
            viewInstruction.setText(ADD_NEW);
        } else if (!currentText.equals(CLICK_TO_VIEW))
        {
            viewInstruction.setText(CLICK_TO_VIEW);
        }
    }

    private void showLoginPrompt(String title, Parent root)
    {
        if (root != null)
        {
            Scene loginPromptScene = new Scene(root, 600, 400);
            loginPromptScene.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(loginPromptScene);
            stage.setOnCloseRequest(event -> setCreatingOrEditingLogin(false));
            stage.show();
        }
    }

    private void setControlsEnabled(boolean enabled)
    {
        toggleViewPassword.setDisable(!enabled);
        closeButton.setDisable(!enabled);
        editLoginInfo.setDisable(!enabled);
        deleteLoginInfo.setDisable(!enabled);
        copyPassword.setDisable(!enabled);
    }

    private ILoginInfoHandler getLoginInfoHandler()
    {
        if (loginInfoHandler == null)
        {
            loginInfoHandler = dependencyInjector.resolve(ILoginInfoHandler.class);
        }

        return loginInfoHandler;
    }

    // endregion
}
