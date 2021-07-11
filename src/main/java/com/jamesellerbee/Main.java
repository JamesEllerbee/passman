package com.jamesellerbee;

import com.jamesellerbee.data.LoginInfoProvider;
import com.jamesellerbee.interfaces.*;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.ui.controller.ElementCardController;
import com.jamesellerbee.ui.controller.MainController;
import com.jamesellerbee.utilities.console.ConsoleException;
import com.jamesellerbee.utilities.console.ConsoleHandler;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import com.jamesellerbee.utilities.properties.PropertyProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main extends Application
{
    private static final String KEY_FILE_NAME = "private.key";
    private static final String CONTENT_FILE_EXTENSION = ".enc";
    private static final String DEFAULT_PATH = System.getProperty("user.dir");
    private static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String SYSTEM_LINE_SEPARATOR = System.lineSeparator();
    private static final String TITLE = "Passman";

    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final String path;
    private final IPropertyProvider propertyProvider;
    private final IConsoleHandler consoleHandler;
    private final IEncryptionEngine encryptionEngine;
    private final ILoginInfoProvider loginInfoProvider;

    public Main()
    {
        propertyProvider = new PropertyProvider();
        path = propertyProvider.get("Path", DEFAULT_PATH);

        initializeDirectory();
        consoleHandler = new ConsoleHandler();
        encryptionEngine = new EncryptionEngine(path + SYSTEM_FILE_SEPARATOR + KEY_FILE_NAME);
        loginInfoProvider = new LoginInfoProvider(encryptionEngine);
    }

    private void initializeDirectory()
    {
        File identifiersPath = new File(path);
        if (identifiersPath.mkdir())
        {
            System.out.println("Directory created.");
        }
    }

    public String processCommand(String command, String[] args)
    {
        String output = "";
        switch (command)
        {
            case "store":
                output = store();
                break;
            case "get":
                output = retrieve(args[1]);
                break;
            case "ls":
            case "list":
                output = loginInfoProvider.retrieveAllAsString(path);
                break;
            case "rm":
                output = remove(args[1]);
                break;
            case "help":
                output = "store: writes a entry\nget <identifier>: outputs contents of store.";
                break;
            default:
                output = "Unrecognized command";
                break;
        }
        return output;
    }

    private String remove(String arg)
    {
        String output = "";
        File toRemove = new File(path + arg + CONTENT_FILE_EXTENSION);
        if (toRemove.exists())
        {
            if (toRemove.delete())
            {
                output = "Done.";
            } else
            {
                System.err.println("Could not delete that id.");
            }
        } else
        {
            System.err.println("That id does not exist.");
        }

        return output;
    }

    private String store()
    {
        String output;

        System.out.print("Enter id: ");
        String name = consoleHandler.readLine();

        System.out.print("Enter user: ");
        name += "_" + consoleHandler.readLine();
        String content = "";
        try
        {
            System.out.print("Enter pass: ");
            content += consoleHandler.readPassword();
        } catch (ConsoleException e)
        {
            logger.error("Cannot securely read password.");
            System.out.print("Do you want to input your password in plain text? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if(response.equalsIgnoreCase("y"))
            {
                System.out.print("password: ");
                content += scanner.nextLine();
            }
            else
            {
                content += "NOT SET";
            }

        }
        finally
        {
            storeContent(content, name);
            output = "Done.";
        }

        return output;
    }

    public void storeContent(String content, String fileName)
    {
        encryptionEngine.encrypt(content, this.path + SYSTEM_FILE_SEPARATOR + fileName + CONTENT_FILE_EXTENSION);
    }

    public String retrieve(String path)
    {
        return encryptionEngine.decrypt(this.path + path + CONTENT_FILE_EXTENSION);
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle(TITLE);

        // main ui
        FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent root = mainFxmlLoader.load();
        MainController mainController = mainFxmlLoader.getController();

        // load existing login information
        List<LoginInfo> loginInfos = loginInfoProvider.getAllLoginInfo(path);
        loginInfos.forEach(loginInfo -> mainController.addContent(ElementCardController.createNewCard(mainController, loginInfo)));

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            Main main = new Main();
            System.out.println(main.processCommand(args[0], args));
        } else
        {
            launch(Main.class, args);
        }
    }
}
