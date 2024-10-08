package org.example.Controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.Model.JwtGenerator;
import org.example.Model.Post;
import org.example.Model.User;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.*;

public class LoginController {
    User user;
    @FXML
    private Label title;
    @FXML
    private Button signup;
    @FXML
    private Label error;
    @FXML
    private PasswordField inputPass;
    @FXML
    private Label password;
    @FXML
    private Label email;
    @FXML
    private TextField inputEmail;
    @FXML
    private ImageView profImage; // = new ImageView(new Image("/ap.png"));


    private String parametrs(){
        return url + "/login/" + inputEmail.getText() + "/" + inputPass.getText();
    }

    @FXML
    private TitledPane left;




    @FXML
    protected void changeToSignup(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("signup.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }



    @FXML
    protected void login(ActionEvent event) throws IOException {
        if (sendGetUserRequest()) {
            goToFeed(event);
        }
    }

    private boolean sendGetUserRequest() throws IOException {
        URL obj = new URL(parametrs());
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to GET
        connection.setRequestMethod("GET");

        System.out.println(parametrs());

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
        writeToFile(token);
        return true;
    }

    public static User GetUserWithTokenTemp(String token) throws IOException {
        User usep;
        URL obj = new URL(url + "/user/" + token);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to GET
        connection.setRequestMethod("GET");

        System.out.println(obj);
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
            System.out.println("Connection Lost!");
        }
        System.out.println("Response Code: " + responseCode);

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
        usep = createUserFromJson(response.toString());
        connection.disconnect();
        return usep;
    }

    private boolean GetUserWithToken(String token) throws IOException {
        URL obj = new URL(url + "/user/" + token);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to GET
        connection.setRequestMethod("GET");

        System.out.println(obj);
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
        if (responseCode == 406) {
            error.setText("Password incorrect!");
            return false;
        } else if (responseCode == 404) {
            error.setText("User Not Found");
            return false;
        } else if (responseCode / 100 <= 0) {
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
        user = createUserFromJson(response.toString());
        connection.disconnect();
        return true;
    }

    public static User createUserFromJson(String json){
        JSONObject jsonObject = new JSONObject(String.valueOf(json));
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
        return new User(firstName, lastName, additionalName, email, title, token, imagePathProfile,
                imagePathBackground, jobId, educationId, connectionInfoId, country, city, profession);

    }


    static void writeToFile(String response){
        try {
            FileWriter writer = new FileWriter("demoLogin\\src\\main\\resources\\tokens.txt");
            writer.write(response);
            writer.flush();
            writer.close();
            System.out.println("Data has been written to the file.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() throws IOException {
        if (readFromFile() != null && JwtGenerator.tokenIsValid(readFromFile())){
            GetUserWithToken(readFromFile());
            goToFeedp(password);
        }
    }

    public void SSOLogin() throws URISyntaxException, IOException {
        String clientId = "Ov23liNK8crEAR6uV5GD";
        String redirectUri = "http://localhost:8888/callback";
        String authUrl = "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=user";

        Desktop.getDesktop().browse(new URI(authUrl));
        String response = SignUpController.RecieveRespond();
        if (!(response.equals("failed") || response.equals(null))){
            SignUpController.ReadyToGoToFeed(response);
            goToFeedp(error);
        }else{
            error.setText("Login failed!");
        }
    }

    public static String readFromFile(){
        String filePath = "demoLogin\\src\\main\\resources\\tokens.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                System.out.println(line);
                return line;
            } else {
                System.out.println("File is empty.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
    }

}