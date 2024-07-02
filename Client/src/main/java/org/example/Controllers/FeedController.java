package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.almasb.fxgl.texture.ImagesKt.writeToFile;
import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.transfer;
import static org.example.Controllers.ParentController.user;


public class FeedController {
    @FXML
    private Label title;

    @FXML
    private VBox list;


    private boolean sendGetRequest() throws IOException {
        URL obj = new URL( url + "/post");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to GET
        connection.setRequestMethod("GET");
        connection.setRequestProperty("");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Getting the response code
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            error.setText("Connection Lost!");
        }
        System.out.println("Response Code: " + responseCode);
        if (responseCode == 406){
            error.setText("Password incorrect!");
            return false;
        }else if (responseCode == 404){
            error.setText("User Not Found");
            return false;
        }else if (responseCode/100 <= 0){
            error.setText("Connection Lost!");
            return false;
        }

        // Reading the response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Printing the response
        System.out.println("Response: " + response);
        JSONObject jsonObject = new JSONObject(String.valueOf(response));
        String token = jsonObject.isNull("token") ? null : jsonObject.getString("token");
        String firstName = jsonObject.isNull("firstName") ? null : jsonObject.getString("firstName");
        String lastName = jsonObject.isNull("lastName") ? null : jsonObject.getString("lastName");
        String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
        String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
        String additionalName = jsonObject.isNull("additionalName") ? null : jsonObject.getString("additionalName");
        String email = jsonObject.isNull("email") ? null : jsonObject.getString("email");
        String title = jsonObject.isNull("title") ? null : jsonObject.getString("title");
        String profession = jsonObject.isNull("profession") ? null : jsonObject.getString("profession");
        String imagePathProfile = jsonObject.isNull("imagePathProfile") ? null : jsonObject.getString("imagePathProfile");
        String imagePathBackground = jsonObject.isNull("imagePathBackground") ? null : jsonObject.getString("imagePathBackground");
        int jobId = jsonObject.isNull("jobId") ? null : jsonObject.getInt("jobId");
        int educationId = jsonObject.isNull("educationId") ? null : jsonObject.getInt("educationId");
        int connectionInfoId = jsonObject.isNull("connectionInfoId") ? null : jsonObject.getInt("connectionInfoId");
        user = new User(firstName,lastName,additionalName , email , title , token , imagePathProfile,
                imagePathBackground , jobId , educationId , connectionInfoId , country , city , profession);
        return true;
    }

    @FXML
    public void initialize() {

        VBox pane = null;
        System.out.println("here");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FeedController.class.getResource("post.fxml"));
            pane = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("it's here");
        }
        list.getChildren().setAll(pane);
    }

    @FXML
    protected void backToLogin(ActionEvent event){
        ParentController.deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }

    @FXML
    protected void goToProfile(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("profile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/profileStyle.css")) , event);
    }


}
