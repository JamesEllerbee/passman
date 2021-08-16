package com.jamesellerbee.data;

import com.jamesellerbee.interfaces.IEncryptionEngine;
import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.interfaces.IPropertyProvider;
import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.ui.controller.LoginInfoCardController;
import com.jamesellerbee.ui.controller.MainController;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.constants.PropertyConstants;
import com.jamesellerbee.utilities.constants.SystemConstants;
import com.jamesellerbee.utilities.logging.SimpleLogger;

import java.io.File;

/**
 * Handles writing and removing login info from system.
 */
public class LoginInfoHandler implements ILoginInfoHandler
{
  private final ILogger logger = new SimpleLogger(getClass().getName());

  private final IInjector dependencyInjector;
  private IEncryptionEngine encryptionEngine;
  private IPropertyProvider propertyProvider;

  public LoginInfoHandler(IInjector dependencyInjector)
  {
    if (dependencyInjector == null)
    {
      logger.error("");
      throw new IllegalArgumentException();
    }

    this.dependencyInjector = dependencyInjector;
  }

  @Override
  public boolean store(LoginInfo loginInfo, boolean newContent)
  {
    boolean result = false;

    IEncryptionEngine encryptionEngine = getEncryptionEngine();
    IPropertyProvider propertyProvider = getPropertyProvider();

    if (encryptionEngine != null)
    {
      String path = SystemConstants.DEFAULT_PATH;
      if (propertyProvider != null)
      {
        path = propertyProvider.get(PropertyConstants.PATH, SystemConstants.DEFAULT_PATH);

        createDirectory(path);
      }

      String fileName = loginInfo.getIdentifier() + EncryptionEngine.FILE_NAME_SEPARATOR + loginInfo.getUsername();

      encryptionEngine.encrypt(loginInfo.getPassword(), path + SystemConstants.SYSTEM_FILE_SEPARATOR + fileName);

      File newStoredInfoFile = new File(path + SystemConstants.SYSTEM_FILE_SEPARATOR + fileName);

      result = newStoredInfoFile.exists();
      if (result)
      {
        logger.info("Stored info created successfully.");

        MainController mainController = dependencyInjector.resolve(MainController.class);
        if (mainController != null)
        {
          if (newContent)
          {
            mainController.addContent(loginInfo.getIdentifier(), LoginInfoCardController.createNewCard(dependencyInjector, loginInfo));
          }
          else
          {
            mainController.updateContent(loginInfo.getIdentifier(), LoginInfoCardController.createNewCard(dependencyInjector, loginInfo));
            mainController.setSelected(loginInfo);
          }
        }
      }
    }
    else
    {
      logger.error("Missing encryption engine dependency.");
    }

    return result;
  }

  @Override
  public boolean remove(LoginInfo loginInfo)
  {
    boolean result = false;

    IPropertyProvider propertyProvider = getPropertyProvider();

    if (propertyProvider != null)
    {
      String path = propertyProvider.get(PropertyConstants.PATH, SystemConstants.DEFAULT_PATH);
      File toRemove = new File(path +
          SystemConstants.SYSTEM_FILE_SEPARATOR +
          loginInfo.getIdentifier() +
          EncryptionEngine.FILE_NAME_SEPARATOR +
          loginInfo.getUsername());
      if (toRemove.exists())
      {
        result = toRemove.delete();
        if (!result)
        {
          logger.warn("Could not delete stored login.");
        }
      }
    }

    return result;
  }

  private void createDirectory(String path)
  {
    File dir = new File(path);
    if (!dir.exists())
    {
      dir.mkdir();
    }
  }

  private IEncryptionEngine getEncryptionEngine()
  {
    if (encryptionEngine == null)
    {
      encryptionEngine = dependencyInjector.resolve(IEncryptionEngine.class);
    }

    return encryptionEngine;
  }

  private IPropertyProvider getPropertyProvider()
  {
    if (propertyProvider == null)
    {
      propertyProvider = dependencyInjector.resolve(IPropertyProvider.class);
    }

    return propertyProvider;
  }
}
