package org.example.Controllers;


import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.goToFeed;
import static org.example.Controllers.ParentController.transfer;

public class SignUpController {
    @FXML
    private Label error;
    @FXML
    private PasswordField firstPass;
    @FXML
    private PasswordField secondPass;
    @FXML
    private Label email;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField inputEmail;
    @FXML
    private Label password;
    @FXML
    private Label repeatPassword;



    @FXML
    protected void backToLogin(ActionEvent event){
        ParentController.deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }

    @FXML
    protected void submit(ActionEvent event) throws IOException {
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
                if (sendPostRequest()) {
                    goToFeed(event);
                }
            } else {
                repeatPassword.setStyle("-fx-text-fill: red");
                password.setStyle("-fx-text-fill: black");
            }
        } else {
            password.setStyle("-fx-text-fill: red");
        }
    }

    private boolean sendPostRequest() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonToObjectOne(jsonObject , firstName.getText() , lastName.getText()
                        , inputEmail.getText() , firstPass.getText());
        // URL of the server endpoint
        // Create HttpURLConnection object
        URL serverUrl = new URL(url + "/signup");
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        // Set request method
        connection.setRequestMethod("POST");
        // Enable output and write JSON data
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Get response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        if (responseCode == 406) {
            error.setText("invalid email or password!!");
            return false;
        }else if (responseCode == 403){
            error.setText("this email already have account");
            return false;
        }
        return true;
    }

    private void jsonToObjectOne(JsonObject jsonObject , String firstName , String lastName
                    , String email , String password ){
        jsonObject.addProperty("firstName" , firstName);
        jsonObject.addProperty("lastName" , lastName);
        jsonObject.addProperty("email" , email);
        jsonObject.addProperty("password" , password);
    }


}
