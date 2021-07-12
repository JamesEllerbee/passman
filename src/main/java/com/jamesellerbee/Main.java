package com.jamesellerbee;

import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.ILoginInfoProvider;
import com.jamesellerbee.interfaces.IPropertyProvider;
import com.jamesellerbee.ui.controller.LoginInfoCardController;
import com.jamesellerbee.ui.controller.MainController;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.constants.PropertyConstants;
import com.jamesellerbee.utilities.constants.SystemConstants;
import com.jamesellerbee.utilities.injection.DependencyLoader;
import com.jamesellerbee.utilities.injection.Injector;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application
{

    private static final String TITLE = "Passman";

    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final IInjector dependencyInjector;

    public Main()
    {
        dependencyInjector = Injector.getInstance();

        DependencyLoader dependencyLoader = new DependencyLoader(dependencyInjector);
        dependencyLoader.load();
    }

    public void start(Stage primaryStage) throws Exception
    {
        // TODO: parse application arguments and add a headless mode.
        primaryStage.setTitle(TITLE);

        // main ui
        FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent root = mainFxmlLoader.load();
        MainController mainController = mainFxmlLoader.getController();
        dependencyInjector.register(MainController.class, mainController);

        // load existing login information
        ILoginInfoProvider loginInfoProvider = dependencyInjector.resolve(ILoginInfoProvider.class);
        IPropertyProvider propertyProvider = dependencyInjector.resolve(IPropertyProvider.class);

        List<LoginInfo> loginInfos = loginInfoProvider.getAllLoginInfo(propertyProvider.get(PropertyConstants.PATH,
                SystemConstants.DEFAULT_PATH));
        loginInfos.forEach(loginInfo -> mainController.addContent(LoginInfoCardController.createNewCard(dependencyInjector, loginInfo)));

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(Main.class, args);
    }
}
