package org.example.demologin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    @FXML
    private ToggleButton like;
    @FXML
    private ImageView likeImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        likeImage.setImage(new Image(getClass().getResourceAsStream("like.png")));
    }

    @FXML
    protected void dolike(){
        if (like.isSelected())
            likeImage.setImage(new Image("like.png"));
        else
            likeImage.setImage(new Image("unlike.png"));
    }
}
