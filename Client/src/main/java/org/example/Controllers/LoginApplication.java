package org.example.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
public class LoginApplication extends Application {
    public final static String url = "http://localhost:8888";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
        stage.setMaximized(true);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alinkedia");
        scene.getStylesheets().add(String.valueOf(getClass().getResource("/cssFiles/Style.css")));
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/pictures/ap.png"))));
        stage.setScene(scene);
        stage.show();
        System.out.println("hello");
    }

    public static void main(String[] args) {
        launch();
    }
}