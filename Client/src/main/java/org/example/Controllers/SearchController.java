package org.example.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.Model.Post;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.ParentController.deleteToken;
import static org.example.Controllers.ParentController.transfer;

public class SearchController {
    @FXML
    private Label title;
    @FXML
    private TextField searchText;
    @FXML
    private GridPane personList;
    @FXML
    private Label searchTarget;


    @FXML
    protected void doSearch() throws IOException {
        String[] tosearch = searchText.getText().split(" ");
        sendSearchReauest(tosearch[0] , tosearch[1]);
    }

    private void sendSearchReauest(String tosearch , String to) throws IOException {
        URL obj = new URL(url + "/search/" + tosearch + "/" + to);
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
        if (response.length() == 0){
            System.out.println("empty");
        }else {
            ArrayList<User> users = users(String.valueOf(response));
            System.out.println(response);
            addToList(users);
            connection.disconnect();
        }
    }
    private void addToList(ArrayList<User> posts){
        // Update the UI on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            int columns = 0;
            int rows = 1;
            personList.getChildren().clear();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(FeedController.class.getResource("person.fxml"));
                VBox addPostBox = fxmlLoader.load();
                personList.add(addPostBox, columns++, rows);
                GridPane.setMargin(addPostBox, new Insets(10));
                for (User i : posts) {
                    FXMLLoader fxmlLoaderq = new FXMLLoader();
                    fxmlLoaderq.setLocation(FeedController.class.getResource("person.fxml"));
                    VBox postBox = fxmlLoaderq.load();
                    PersonController personController = new PersonController();
                    personController.setParts(i);
//                    postController.setData(posts.get(i));
                    if (columns == 1) {
                        columns = 0;
                        rows++;
                    }
                    personList.add(postBox, columns++, rows);
                    GridPane.setMargin(postBox, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private ArrayList<User> users(String tmp){
        ArrayList<User> posts = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(tmp).getAsJsonArray();
        if (!jsonArray.isEmpty()) {
            for (JsonElement e : jsonArray) {
                posts.add(createUserFromJson(e.toString()));
            }
        }
        return posts;
    }
    private User createUserFromJson(String json){
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
        User usert = new User(firstName,lastName,additionalName , email , title , token , imagePathProfile,
                imagePathBackground , jobId , educationId , connectionInfoId , country , city , profession);
        return usert;
    }

    @FXML
    protected void backToLogin(ActionEvent event){
        deleteToken();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/Style.css")) , event);
    }
    @FXML
    protected void goToFeed(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("demofeed.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/feedStyle.css")) , event);
    }
}
