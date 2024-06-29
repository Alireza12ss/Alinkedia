package org.example.demologin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
        stage.setMaximized(true);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alinkdia");
        scene.getStylesheets().add("/Style.css");
        stage.getIcons().add(new Image("/ap.png"));
        stage.setScene(scene);
        stage.show();
        System.out.println("hello");
    }

    public static void main(String[] args) {
        launch();
    }
}