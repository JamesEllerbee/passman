package com.jamesellerbee;

import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.utilities.ConsoleException;
import com.jamesellerbee.utilities.ConsoleHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{
  private static final String KEY_FILE_NAME = "private.key";
  private static final String CONTENT_FILE_EXTENSION = ".enc";
  private static final String PATH_PREFIX = "C:\\Users\\ellerbeej\\.directory\\";
  private ConsoleHandler consoleHandler;
  private EncryptionEngine encryptionEngine;

  public Main()
  {
    initializeDirectory();
    consoleHandler = new ConsoleHandler();
    encryptionEngine = new EncryptionEngine(PATH_PREFIX + KEY_FILE_NAME);
  }

  private void initializeDirectory()
  {
    File identifiersPath = new File(PATH_PREFIX);
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
        output = retrieveContent(args[1]);
        break;
      case "list":
        output = retrieveAll();
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

  private String store()
  {
    String output = "";

    System.out.print("Enter id: ");
    String name = consoleHandler.readLine();

    System.out.print("Enter user: ");
    String content = "user: " + consoleHandler.readLine() + "\n";
    try
    {
      content += "pass: " + consoleHandler.readPassword();
      storeContent(content, name);

      output = "Done.";
    }
    catch (ConsoleException e)
    {
      System.err.println("Cannot securely read password.");
    }

    return output;
  }

  public void storeContent(String content, String path)
  {
    encryptionEngine.encrypt(content, PATH_PREFIX + path + CONTENT_FILE_EXTENSION);
  }

  public String retrieveContent(String path)
  {
    return encryptionEngine.decrypt(PATH_PREFIX + path + CONTENT_FILE_EXTENSION);
  }

  public String retrieveAll()
  {
    String output = "";
    try
    {
      List<File> files = Files.list(Paths.get(PATH_PREFIX))
          .map(Path::toFile)
          .filter(file -> !file.getName().contains(".key"))
          .collect(Collectors.toList());

      StringBuilder stringBuilder = new StringBuilder();

      if(files.isEmpty())
      {
        stringBuilder.append("Nothing to list. Try storing content.");
      }

      files.forEach(file -> stringBuilder.append(encryptionEngine.decrypt(file.getAbsolutePath())));

      output = stringBuilder.toString();
    }
    catch (IOException e)
    {
      System.err.println("Error while reading the directory.");
    }

    return output;
  }

  public static void main(String[] args)
  {
    Main main = new Main();
    if (args.length > 0)
    {
      System.out.println(main.processCommand(args[0], args));
    }
  }
}
