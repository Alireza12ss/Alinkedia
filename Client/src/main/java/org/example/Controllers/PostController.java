package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.Model.Post;

import static org.example.Controllers.ParentController.transfer;

public class PostController {

    Post post ;
    @FXML
    private Label title;

    @FXML
    private TextField comment;

    @FXML
    private ToggleButton like;

    @FXML
    private ImageView likeImage;

    @FXML
    private ImageView picture;

    @FXML
    private TextArea text;

    @FXML
    protected void goToProfile(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("profile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/profileStyle.css")) , event);
    }

    @FXML
    public void initialize(){

    }

    @FXML
    protected void doLike(){
        if (like.isSelected())
            likeImage.setImage(new Image(getClass().getResource("/pictures/like.png").toString()));
        else
            likeImage.setImage(new Image(getClass().getResource("/pictures/unlike.png").toString()));
    }
}
