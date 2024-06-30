package org.example.demologin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FeedController implements Initializable {
    @FXML
    private VBox list;
    @FXML
    private Label title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    protected void test(){

    }
}
