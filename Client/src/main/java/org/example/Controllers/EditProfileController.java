package org.example.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.transfer;
import static org.example.Controllers.ProfileController.user;


public class EditProfileController {


    @FXML
    private StackPane backImage;

    @FXML
    private TextArea descriptionLabel;

    @FXML
    private TextField firstNameLabel;

    @FXML
    private TextField industryLabel;

    @FXML
    private TextField lastNameLabel;

    @FXML
    private TextField AdditionalName;
    @FXML
    private TextField countryLabel;
    @FXML
    private TextField cityLabel;
    @FXML
    private ImageView savePhoto;
    @FXML
    private DatePicker birthday;

    @FXML
    private GNAvatarView profileImage;

    @FXML
    private TextField schoolLabel;

    @FXML
    private Button save;

    @FXML
    void BackToProfile(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("profile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/profileStyle.css")) , event);
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        user.setFirstName(firstNameLabel.getText());
        user.setLastName(lastNameLabel.getText());
        user.setAdditionalName(AdditionalName.getText());
        LocalDate localDate1 = birthday.getValue();
        Date newd = localDate1 == null ? null : Date.valueOf(localDate1);
        user.setBirthDay(String.valueOf(newd));
        user.setCity(cityLabel.getText());
        user.setCountry(countryLabel.getText());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject jsonObjectq = new JsonObject();
        jsonToObjectOne(jsonObjectq , user.getEmail() == null ? "" : user.getEmail() , user.getFirstName() == null ? "" : user.getFirstName(),
                user.getLastName() == null ? "" : user.getLastName() , user.getAdditionalName()== null ? "" : user.getAdditionalName(),
                user.getTitle() == null ? "" : user.getTitle(), user.getImagePathProfile() == null ? "" : user.getImagePathProfile(),
                user.getImagePathBackground() == null ? "" : user.getImagePathBackground(),  user.getCountry() == null ? "" : user.getCountry(),
                user.getCity() == null ? "" : user.getCity(), user.getProfession() == null ? "" : user.getProfession(),
                user.getBirthDay() == "null" ? "" : user.getBirthDay());
        if (sendPUTRequest(jsonObjectq)){
            save.setText("Saved!");
            savePhoto.setVisible(false);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else {
            save.setText("Save Failed!");
            savePhoto.setVisible(false);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        BackToProfile(event);
    }
    private boolean sendPUTRequest(JsonObject jsonObject) throws IOException {
        // URL of the server endpoint
        // Create HttpURLConnection object
        URL serverUrl = new URL(url + "/user");
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        // Set request method
        connection.setRequestMethod("PUT");
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
        System.out.println("Response Code: " + responseCode + " oppop");
        if (responseCode/100 != 2) {
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

        return true;
    }

    private void jsonToObjectOne(JsonObject jsonObject , String email , String firstName, String lastName, String additionalName
            , String title, String imagePathProfile,
                                 String imagePathBackground, String country, String city, String profession  , String birthday){
        jsonObject.addProperty("email" , email);
        jsonObject.addProperty("firstName" , firstName);
        jsonObject.addProperty("lastName" , lastName);
        jsonObject.addProperty("email" , email);
        jsonObject.addProperty("additionalName" , additionalName);
        jsonObject.addProperty("title" , title);
        jsonObject.addProperty("imagePathProfile" , imagePathProfile);
        jsonObject.addProperty("imagePathBackground" , imagePathBackground);
        jsonObject.addProperty("country" , country);
        jsonObject.addProperty("city" , city);
        jsonObject.addProperty("profession" , profession);
        jsonObject.addProperty("birthday" , birthday);

    }


    @FXML
    public void initialize(){
        if (user.getImagePathProfile() == null){
            profileImage.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            profileImage.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathProfile()).toString()));
        }
        if (user.getImagePathBackground() == null){
            backImage.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/pictures/defaultBack.jpg").toString())
                    , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT)));

            Rectangle clip = new Rectangle();
            clip.setWidth(92.0f);
            clip.setHeight(120.0f);
            clip.setArcHeight(25);
            clip.setArcWidth(10);
            clip.setStroke(Color.BLACK);
            backImage.setClip(clip);


        }else {
            backImage.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/pictures/" + user.getImagePathBackground()).toString())
                    , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT)));
        }
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        AdditionalName.setText(user.getAdditionalName());
        descriptionLabel.setText(user.getTitle());
        birthday.setAccessibleText(user.getBirthDay());
        countryLabel.setText(user.getCountry());
        cityLabel.setText(user.getCity());
    }

    @FXML
    protected void selectImage(ActionEvent event){
        URL path = null;
        try {
            path = chooseFile(event);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (path == null) return;
        profileImage.setImage(new Image(String.valueOf(path)));
    }

    @FXML
    protected void selectBackImage(ActionEvent event){
        URL path = null;
        try {
            path = chooseFile(event);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (path == null) return;
        backImage.setBackground(new Background(new BackgroundImage(new Image(String.valueOf(path)) , BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    private URL chooseFile(ActionEvent event) throws MalformedURLException {
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            return file.toURL();
        else
            return null;
    }
}

