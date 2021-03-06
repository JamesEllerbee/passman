package com.jamesellerbee.data;

import com.jamesellerbee.interfaces.IEncryptionEngine;
import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoProvider;
import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.logging.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods for retrieving and storing login data from the file system.
 */
public class LoginInfoProvider implements ILoginInfoProvider
{
    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final IInjector dependencyInjector;
    private IEncryptionEngine encryptionEngine;

    public LoginInfoProvider(IInjector dependencyInjector)
    {
        if (dependencyInjector == null)
        {
            logger.error("Dependency injector cannot be null.");
            throw new IllegalArgumentException();
        }



        this.dependencyInjector = dependencyInjector;
    }

    @Override
    public List<LoginInfo> getAllLoginInfo(String path)
    {
        List<LoginInfo> loginInfos = new ArrayList<>();

        IEncryptionEngine encryptionEngine = getEncryptionEngine();
        if (encryptionEngine != null)
        {
            try
            {
                List<File> files = Files.list(Paths.get(path))
                                        .map(Path::toFile)
                                        .filter(file -> !file.getName().contains(EncryptionEngine.KEY_FILE_EXTENSION))
                                        .collect(Collectors.toList());

                files.forEach(file ->
                              {
                                  String[] tokens = file.getName()
                                                        .replaceAll(EncryptionEngine.LOGIN_FILE_EXTENSION, "")
                                                        .split(EncryptionEngine.FILE_NAME_SEPARATOR);

                                  String id = tokens[0];
                                  String username = tokens[1];
                                  String password = encryptionEngine.decrypt(file.getAbsolutePath());

                                  loginInfos.add(new LoginInfo(id, username, password));
                              });
            }
            catch (IOException e)
            {
                System.err.println("Error while reading the directory.");
            }
        }
        else
        {
            logger.error("Missing encryption engine dependency.");
        }

        return loginInfos;
    }

    @Override
    public String retrieveAllAsString(String path)
    {
        String output = "";

        IEncryptionEngine encryptionEngine = getEncryptionEngine();
        if (encryptionEngine != null)
        {
            try
            {
                List<File> files = Files.list(
                        Paths.get(path)).map(Path::toFile)
                                        .filter(file -> !file.getName().contains(EncryptionEngine.KEY_FILE_EXTENSION))
                                        .collect(Collectors.toList());

                StringBuilder stringBuilder = new StringBuilder();

                if (files.isEmpty())
                {
                    stringBuilder.append("Nothing to list. Try storing content.");
                }

                files.forEach(file -> stringBuilder.append(
                        file.getName().replaceAll(EncryptionEngine.LOGIN_FILE_EXTENSION, "\t"))
                                                   .append(encryptionEngine.decrypt(file.getAbsolutePath()))
                                                   .append("\n"));

                output = stringBuilder.toString();
            }
            catch (IOException e)
            {
                logger.error("Error while reading the directory.");
            }
        }
        else
        {
            logger.error("Missing encryption engine dependency.");
        }

        return output;
    }

    private IEncryptionEngine getEncryptionEngine()
    {
        if (encryptionEngine == null)
        {
            encryptionEngine = dependencyInjector.resolve(IEncryptionEngine.class);
        }

        return encryptionEngine;
    }
}
