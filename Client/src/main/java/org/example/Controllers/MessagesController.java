package org.example.Controllers;

import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class MessagesController {

    @FXML
    private ScrollPane messageList;

    @FXML
    private TextArea messageText;

    @FXML
    private GNAvatarView profileImage;

    @FXML
    private Label sector;

    @FXML
    private Button selecterMedia;

    @FXML
    private Button send;

    @FXML
    private ImageView sendVoices;

    @FXML
    private Label title;

    @FXML
    private Button userName;

    @FXML
    void goToProfile(ActionEvent event) {

    }

}
