package org.example.demologin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label title;
    @FXML
    private Button signup;
    @FXML
    private Button backToLogin;
    @FXML
    private Label guide;
    @FXML
    private PasswordField firstPass;
    @FXML
    private PasswordField secondPass;
    @FXML
    private Label password;
    @FXML
    private Label repeatPassword;
    @FXML
    private Label email;
    @FXML
    private TextField inputEmail;
    @FXML
    private ImageView profImage; // = new ImageView(new Image("/ap.png"));
    @FXML
    private TitledPane left;


    @FXML
    protected void changeToSignup() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("signup.fxml"));
        transfer(fxmlLoader, "/Style.css");
    }

    @FXML
    protected void changeToLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, "/Style.css");
    }

    private void transfer(FXMLLoader fxmlLoader, String styleSheet){
        System.out.println(styleSheet);
        new Thread(() -> {
            final Scene newScene;
            try {
                newScene = new Scene(fxmlLoader.load(), title.getScene().getWidth(), title.getScene().getHeight());
                newScene.getStylesheets().add(styleSheet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                Stage currentStage = (Stage)title.getScene().getWindow();
                currentStage.setScene(newScene);
            });
        }).start();
    }

    @FXML
    protected void submit(){
        if (DataCheck.CheckEmail(inputEmail.getText())){
            email.setStyle("-fx-text-fill: black");
        } else {
            email.setStyle("-fx-text-fill: red");
            return;
        }
        if (DataCheck.CheckPass(firstPass.getText())){
            if (firstPass.getText().equals(secondPass.getText())) {
                password.setStyle("-fx-text-fill: black");
                repeatPassword.setStyle("-fx-text-fill: black");
                test();
            } else {
                repeatPassword.setStyle("-fx-text-fill: red");
                password.setStyle("-fx-text-fill: black");
            }
        } else {
            password.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    protected void test(){
//        profImage.setClip(new Circle(150));
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("demoFeed.fxml"));
        transfer(fxmlLoader, "/feedStyle.css");
//        styleSheet = "/darkStyle.css";
//        title.getScene().getStylesheets().add(styleSheet);
    }

    protected void anotest(){
        
    }
}