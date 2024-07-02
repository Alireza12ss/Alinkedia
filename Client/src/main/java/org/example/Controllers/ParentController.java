package org.example.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.Model.User;

import java.io.*;

public class ParentController {
    static User user;
    public static void transfer(FXMLLoader fxmlLoader, String styleSheet, ActionEvent event){
        System.out.println(styleSheet);
        Node node = ((Node) event.getSource());
        new Thread(() -> {
            final Scene newScene;
            try {
                newScene = new Scene(fxmlLoader.load(), node.getScene().getWidth(), node.getScene().getHeight());
                newScene.getStylesheets().add(styleSheet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                Stage currentStage = (Stage)node.getScene().getWindow();
                currentStage.setScene(newScene);
            });
        }).start();
    }

    public static void transferp(FXMLLoader fxmlLoader, String styleSheet , Label label){
        System.out.println(styleSheet);
        new Thread(() -> {
            final Scene newScene;
            try {
                newScene = new Scene(fxmlLoader.load(), label.getScene().getWidth(), label.getScene().getHeight());
                newScene.getStylesheets().add(styleSheet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                Stage currentStage = (Stage)label.getScene().getWindow();
                currentStage.setScene(newScene);
            });
        }).start();
    }

    @FXML
    public static void goToFeed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("demoFeed.fxml"));
        transfer(fxmlLoader, String.valueOf(LoginController.class.getResource("/cssFiles/feedStyle.css")) , event);
    }
    @FXML
    public static void goToFeedp(Label label) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("demoFeed.fxml"));
        transferp(fxmlLoader, String.valueOf(LoginController.class.getResource("/cssFiles/feedStyle.css")) , label);
    }

    protected static void deleteToken(){
        try {
            // Specify the file path
            File file = new File("demoLogin\\src\\main\\resources\\tokens.txt");

            // Read the contents of the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            StringBuilder content = new StringBuilder();
            boolean firstLine = true;

            while (line != null) {
                if (!firstLine) {
                    content.append(line).append("\n");
                }
                firstLine = false;
                line = reader.readLine();
            }
            reader.close();

            // Write the modified content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            System.out.println("First line deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting first line: " + e.getMessage());
        }
    }
}
