package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.Controllers.ParentController.transfer;


public class FeedController {
    @FXML
    private Button logOut;
    @FXML
    private Label title;

    @FXML
    private VBox list;

    @FXML
    public void initialize() {
        VBox pane = null;
        System.out.println("here");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FeedController.class.getResource("fakepost.fxml"));
            pane = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("it's here");
        }
        list.getChildren().setAll(pane);
    }

    @FXML
    protected void backToLogin(){
        ParentController.deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, "/Style.css" , title);
    }

    @FXML
    protected void goToProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
