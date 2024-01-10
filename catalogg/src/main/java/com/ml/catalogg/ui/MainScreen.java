package com.ml.catalogg.ui;

import com.ml.catalogg.helper.PropertiesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.io.IOException;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@ComponentScan({"com.ml.catalogg*"})
@EntityScan("com.ml.catalogg*")
@EnableJpaRepositories("com.ml.catalogg*")
public class MainScreen extends Application {
    private ApplicationContext context;
    static MainScreenController mainScreenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        context = new SpringApplicationBuilder(MainScreen.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainScreen.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("atalOgg");
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getClassLoader().getResource("logo/logo.png"))));
            PropertiesManager propertiesManager = new PropertiesManager();
            propertiesManager.loadProperties();
            if (propertiesManager.isStartFullscreen()) {
                primaryStage.setMaximized(true);
            }
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
