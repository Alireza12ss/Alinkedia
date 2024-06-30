package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.example.Model.JwtGenerator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.goToFeed;
import static org.example.Controllers.ParentController.transfer;

public class LoginController {
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
    protected void changeToSignup() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("signup.fxml"));
        transfer(fxmlLoader, "/Style.css" , error);
    }



    @FXML
    protected void login() throws IOException {
        if (sendGetRequest()) {
            goToFeed( password);
        }
    }

    private boolean sendGetRequest() throws IOException {
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
        writeToFile(String.valueOf(response));
        return true;
    }

    private void writeToFile(String response){
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
            goToFeed(password);
        }
    }

    private String readFromFile(){
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