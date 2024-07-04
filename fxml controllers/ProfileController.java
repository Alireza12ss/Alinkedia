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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static org.example.Controllers.ParentController.*;

public class ProfileController {
    @FXML
    private ImageView backgroundImage;
    @FXML
    private GNAvatarView profile;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private void initialize(){
        profile.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        Image im = new Image(getClass().getResource("/pictures/ap.png").toString());

        backgroundImage.setImage(im);


//        if (user.getImagePathProfile() == null){
//            profile.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
//        }else {
//            profile.setImage(new Image(user.getImagePathProfile().toString()));
//        }
//        if (user.getImagePathBackground() == null){
//
//
//
//        }else {
//            backgroundImage.setBackground(new Background(new BackgroundImage(new Image(user.getImagePathBackground().toString()) , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                    BackgroundSize.DEFAULT)));
//        }
//        description.setText(user.getFirstName() + " " + user.getLastName() + "\n"
//                    + user.getTitle() + "\n" + user.getCity() + " , " + user.getCountry());
    }

    @FXML
    protected void backToLogin(ActionEvent event){
        deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }
    @FXML
    protected void goToFeed(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("demofeed.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/feedStyle.css")) , event);
    }

}
