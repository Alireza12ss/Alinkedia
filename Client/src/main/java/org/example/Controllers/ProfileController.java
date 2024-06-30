package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import static org.example.Controllers.ParentController.transfer;

public class ProfileController {
    @FXML
    private Label title;
    @FXML
    protected void backToLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, "/Style.css" , title);
    }

}
