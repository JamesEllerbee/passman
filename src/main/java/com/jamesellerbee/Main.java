package com.jamesellerbee;

import com.jamesellerbee.interfaces.IConsoleHandler;
import com.jamesellerbee.interfaces.IEncryptionEngine;
import com.jamesellerbee.interfaces.IPropertyProvider;
import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.ui.Controller.ElementCardController;
import com.jamesellerbee.ui.Controller.MainController;
import com.jamesellerbee.utilities.console.ConsoleException;
import com.jamesellerbee.utilities.console.ConsoleHandler;
import com.jamesellerbee.utilities.properties.PropertyProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application
{
    private static final String KEY_FILE_NAME = "private.key";
    private static final String CONTENT_FILE_EXTENSION = ".enc";
    private static final String DEFAULT_PATH = System.getProperty("user.dir");
    private static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String TITLE = "Passman";

    private final String path;
    private IPropertyProvider propertyProvider;
    private IConsoleHandler consoleHandler;
    private IEncryptionEngine encryptionEngine;

    public Main()
    {
        propertyProvider = new PropertyProvider();
        path = propertyProvider.get("Path", DEFAULT_PATH);

        initializeDirectory();
        consoleHandler = new ConsoleHandler();
        encryptionEngine = new EncryptionEngine(path + SYSTEM_FILE_SEPARATOR + KEY_FILE_NAME);
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
                output = retrieveAll();
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
        String output = "";

        System.out.print("Enter id: ");
        String name = consoleHandler.readLine();

        System.out.print("Enter user: ");
        String content = "user: " + consoleHandler.readLine() + " ";
        try
        {
            System.out.print("Enter pass: ");
            content += "pass: " + consoleHandler.readPassword();
            storeContent(content, name);

            output = "Done.";
        } catch (ConsoleException e)
        {
            System.err.println("Cannot securely read password.");
        }

        return output;
    }

    public void storeContent(String content, String path)
    {
        encryptionEngine.encrypt(content, this.path + path + CONTENT_FILE_EXTENSION);
    }

    public String retrieve(String path)
    {
        return encryptionEngine.decrypt(this.path + path + CONTENT_FILE_EXTENSION);
    }

    public String retrieveAll()
    {
        String output = "";
        try
        {
            List<File> files = Files.list(Paths.get(path))
                    .map(Path::toFile)
                    .filter(file -> !file.getName().contains(".key"))
                    .collect(Collectors.toList());

            StringBuilder stringBuilder = new StringBuilder();

            if (files.isEmpty())
            {
                stringBuilder.append("Nothing to list. Try storing content.");
            }

            files.forEach(file -> stringBuilder.append(file.getName().replaceAll(".enc", "\t")).append(encryptionEngine.decrypt(file.getAbsolutePath())).append("\n"));

            output = stringBuilder.toString();
        } catch (IOException e)
        {
            System.err.println("Error while reading the directory.");
        }

        return output;
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle(TITLE);

        // main ui
        FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent root = mainFxmlLoader.load();
        MainController mainController = mainFxmlLoader.getController();

        // element card fragment test
        FXMLLoader elementCardFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("elementCard.fxml"));
        Parent testElementCard = elementCardFxmlLoader.load();
        ElementCardController elementCardController = elementCardFxmlLoader.getController();

        mainController.addContent(testElementCard);

        primaryStage.setScene(new Scene(root, 300, 250));
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
