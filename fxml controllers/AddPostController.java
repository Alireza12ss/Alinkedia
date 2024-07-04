package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class AddPostController {
    @FXML
    private VBox postNode;
    @FXML
    private Button addMedia;
    @FXML
    private Button done;
    @FXML
    private Button cancel;
    @FXML
    private ImageView media;

    @FXML
    public void initialize(){
        postNode.getChildren().remove(media);
        cancel.setVisible(false);
    }

    @FXML
    protected void selectMedia(ActionEvent event) throws MalformedURLException {
        String path;
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            path = String.valueOf(file.toURL());
        else
            return;
        media.setImage(new Image(String.valueOf(path)));
        media.setFitWidth(596);
        media.setFitHeight(600);
        postNode.getChildren().add(1, media);
        cancel.setVisible(true);
    }

    @FXML
    protected void cancelAction(){
        postNode.getChildren().remove(media);
        cancel.setVisible(false);
    }
}
