package org.example.Controllers;


import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.Model.User;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalTime;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.*;

public class SignUpController {
    static User user;
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Sign up completed !");
                    alert.show();

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
        JsonObject jsonObjectq = new JsonObject();
        jsonToObjectOne(jsonObjectq , firstName.getText() , lastName.getText()
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
            byte[] input = jsonObjectq.toString().getBytes(StandardCharsets.UTF_8);
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
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response: " + response);
        ReadyToGoToFeed(String.valueOf(response));
        return true;
    }

    static void ReadyToGoToFeed(String response) {
        JSONObject jsonObject = new JSONObject(response);
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
        LoginController.writeToFile(String.valueOf(token));
    }

    private void jsonToObjectOne(JsonObject jsonObject , String firstName , String lastName
                    , String email , String password ){
        jsonObject.addProperty("firstName" , firstName);
        jsonObject.addProperty("lastName" , lastName);
        jsonObject.addProperty("email" , email);
        jsonObject.addProperty("password" , password);
    }

    public void SSOLogin() throws URISyntaxException, IOException {
        String clientId = "Ov23liNK8crEAR6uV5GD";
        String redirectUri = "http://localhost:8888/callback";
        String authUrl = "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=user";

        Desktop.getDesktop().browse(new URI(authUrl));

        System.out.println(LocalTime.now());
        String response = RecieveRespond();
        if (!response.equals("failed")){
            ReadyToGoToFeed(response);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Sign up completed !");
            alert.show();
            goToFeedp(error);
        };
    }

    static String RecieveRespond(){
        String hostname = "localhost";  // Replace with the server's hostname or IP address
        int port = 8887;  // Port number to connect to
        int maxRetries = 10;  // Maximum number of retries
        int retryCount = 0;  // Counter for retry attempts

        while (retryCount < maxRetries) {
            try {
                // Attempt to create a socket and connect to the server
                Socket socket = new Socket(hostname, port);
                System.out.println("Connected to the server");

                // If connected, handle communication here
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String message = reader.readLine();
                System.out.println("Message from server: " + message);


                // Close the socket after communication
                socket.close();
                return message;
               // Exit the loop once connected successfully

            } catch (ConnectException e) {

                retryCount++;
                System.out.println("Connection failed. Retrying in 3 seconds... (" + retryCount + "/" + maxRetries + ")");

                try {
                    Thread.sleep(3000);  // Wait for 3 seconds before retrying
                } catch (InterruptedException ie) {

                    Thread.currentThread().interrupt();  // Reset the interrupt status
                    System.out.println("Thread interrupted during sleep");

                }


            } catch (IOException e) {
                System.out.println("Client exception: " + e.getMessage());
                e.printStackTrace();
                break;  // Exit the loop on other IOExceptions
            }
        }

        if (retryCount == maxRetries) {
            System.out.println("Unable to connect to the server after " + maxRetries + " attempts. Exiting.");
            return "failed";
        }

        return "failed";
    }


}
