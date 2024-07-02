package org.example.Controllers;

import io.github.gleidson28.GNAvatarView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static org.example.Controllers.ParentController.*;

public class ProfileController {
    @FXML
    private ImageView background;
    @FXML
    private GNAvatarView profile;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private void initialize(){
        if (user.getImagePathProfile() == null){
            profile.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            profile.setImage(new Image(user.getImagePathProfile().toString()));
        }
        if (user.getImagePathBackground() == null){

            Image im = new Image(getClass().getResource("/pictures/defaultBack.jpg").toString());
            background.setImage(im);

            Rectangle clip = new Rectangle();
            clip.setWidth(92.0f);
            clip.setHeight(120.0f);
            clip.setArcHeight(25);
            clip.setArcWidth(10);
            clip.setStroke(Color.BLACK);
            background.setClip(clip);


        }else {
            background.setImage(new Image(user.getImagePathBackground().toString()));
        }
        description.setText(user.getFirstName() + " " + user.getLastName() + "\n"
                    + user.getTitle() + "\n" + user.getCity() + " , " + user.getCountry());
    }

    @FXML
    protected void backToLogin(ActionEvent event){
        deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }

}
