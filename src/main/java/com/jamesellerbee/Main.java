package com.jamesellerbee;

import com.jamesellerbee.security.EncryptionEngine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main
{
    private static final String KEY_LOCATION = "private.key";
    private static final String FILE_EXTENSION = ".enc";
    private static final String PATH_PREFIX = "/.directory/";
    private Scanner scanner;
    private EncryptionEngine encryptionEngine;



    public Main()
    {
        encryptionEngine = new EncryptionEngine(KEY_LOCATION);
        scanner = new Scanner(System.in);
        initialize();
    }

    private void initialize()
    {
        File identifiersPath = new File(PATH_PREFIX);
        identifiersPath.mkdir();
    }

    public void storeContent(String content, String path)
    {
        encryptionEngine.encrypt(content, PATH_PREFIX + path + FILE_EXTENSION);
    }

    public String retrieveContent(String path)
    {
        return encryptionEngine.decrypt(PATH_PREFIX + path + FILE_EXTENSION);
    }

    public String processCommand(String command, String[] args)
    {
        String output = "";
        switch (command)
        {
            case "store":
                System.out.print("Enter name: ");
                String path = scanner.nextLine();
                System.out.print("Enter user: ");
                String content = "user: " + scanner.nextLine();
                System.out.print("Enter pass: ");
                content += "pass: " + scanner.nextLine();
                storeContent(content, PATH_PREFIX + path + FILE_EXTENSION);
                output = "Done.";
                break;
            case "get":
                output = retrieveContent(PATH_PREFIX + args[1] + FILE_EXTENSION);
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

    public static void main(String[] args)
    {
        Main main = new Main();
        if(args.length > 0)
        {
          System.out.println(main.processCommand(args[0], args));
        }
    }

}
