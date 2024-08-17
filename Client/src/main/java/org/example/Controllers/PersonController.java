package org.example.Controllers;

import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import org.example.Model.User;

import java.io.IOException;

import static org.example.Controllers.OthersProfileController.emailOthers;
import static org.example.Controllers.OthersProfileController.followed;
import static org.example.Controllers.ParentController.transfer;
import static org.example.Controllers.PostController.sendFollowRequest;

public class PersonController {
    static User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    private  GNAvatarView profile;

    @FXML
    private Button name;

    public void setParts(User user){
        currentUser = user;
        if (user.getImagePathProfile() == null){
            profile.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            profile.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathProfile()).toString()));
        }
        name.setText(currentUser.getFirstName() + " " + currentUser.getAdditionalName() + " " + currentUser.getLastName());
    }

    @FXML
    protected void goOtherProfile(ActionEvent event) throws IOException {
        emailOthers = currentUser.getEmail();
        int response = sendFollowRequest(currentUser.getEmail());
        if (response == 200){

        }else if (response == 201){

        }
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("othersProfile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/profileStyle.css")) , event);
    }

}
