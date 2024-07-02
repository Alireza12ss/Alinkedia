package org.example.demologin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class EditProfileController {

    @FXML
    private StackPane backImage;
    @FXML
    private Parent profileImage;

    @FXML
    public void initialize(){
        backImage.setBackground(new Background(new BackgroundImage(new Image("ap.png") , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    @FXML
    protected void selectImage(ActionEvent event){
        URL path = null;
        try {
            path = chooseFile(event);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (path == null) return;
    }

    @FXML
    protected void selectBackImage(ActionEvent event){
        URL path = null;
        try {
            path = chooseFile(event);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (path == null) return;
        backImage.setBackground(new Background(new BackgroundImage(new Image(String.valueOf(path)) , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    private URL chooseFile(ActionEvent event) throws MalformedURLException {
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            return file.toURL();
        else
            return null;
    }
}
