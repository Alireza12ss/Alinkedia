package org.example.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.gleidson28.GNAvatarView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.Model.Post;
import org.example.Model.User;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.example.Controllers.FeedController.createPostFromJson;
import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.LoginController.GetUserWithTokenTemp;
import static org.example.Controllers.LoginController.readFromFile;
import static org.example.Controllers.ParentController.*;

public class ProfileController {
    static User user;
    @FXML
    private GridPane postListView;

    @FXML
    private ImageView background;
    @FXML
    private GNAvatarView profile;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private void initialize() throws IOException {
        user = GetUserWithTokenTemp(readFromFile());
        if (user.getImagePathProfile() == null){
            profile.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            profile.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathProfile()).toString()));
        }
        if (user.getImagePathBackground() == null){

            Image im = new Image(getClass().getResource("/pictures/defaultBack.jpg").toString());

            background.setImage(im);

            Rectangle clip = new Rectangle();
            clip.setWidth(92.0f);
            clip.setHeight(120.0f);
            clip.setArcHeight(25);
            clip.setArcWidth(10);
            clip.setStroke(Color.BLACK);
            background.setClip(clip);
        }else {
            background.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathBackground()).toString()));
        }
        description.setText(getString());


        String response = sendGetPostRequest(readFromFile());
        ArrayList<Post> posts = new ArrayList<>();
        JsonParser parser = new JsonParser();
        System.out.println(response);
        JsonArray jsonArray = parser.parse(response).getAsJsonArray();
        if (!jsonArray.isEmpty()) {
            for (JsonElement e : jsonArray) {
                posts.add(createPostFromJson(e.toString()));
            }
        }
        addToList(posts);
    }

    public @NotNull String getString() {
        String tmp = user.getFirstName() == null ? "" : user.getFirstName();
        tmp = tmp.concat(user.getAdditionalName() == null ? " " : " " + user.getAdditionalName());
        tmp = tmp.concat(user.getLastName() == null ? " " : " " + user.getLastName() + "\n");
        tmp = tmp.concat(user.getTitle() == null ? "" : user.getTitle() + "\n");
        tmp = tmp.concat(user.getCountry() == null ? "" : user.getCountry() + " , ");
        tmp = tmp.concat(user.getCity() == null ? "" : user.getCity() + "\n");
        return tmp;
    }

    @FXML
    protected void editProfile(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("editProfile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/editProfileStyle.css")) , event);
    }

    public static String sendGetPostRequest(String token) throws IOException {
        URL obj = new URL(url + "/post");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to POST
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", token);
        // Set content type if needed
        // Set read timeout

        System.out.println("URL: " + connection.getURL());

        // Getting the response code
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("Error getting response code: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for better debugging
            throw new IOException("Failed to get response code", e);
        }
        System.out.println("Response Code: " + responseCode);

        // Reading the response
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for better debugging
            throw new IOException("Failed to get input stream", e);
        }
        String inputLine;
        StringBuilder response = new StringBuilder();


        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response: " + response);
        // Printing the response
        return response.toString();
    }

    private void addToList(ArrayList<Post> posts){
        // Update the UI on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            int columns = 0;
            int rows = 1;
            postListView.getChildren().clear();

            try {
                FXMLLoader fxmlLoaderq = new FXMLLoader();
                fxmlLoaderq.setLocation(FeedController.class.getResource("addPost.fxml"));
                VBox addPostBox = fxmlLoaderq.load();
                postListView.add(addPostBox, columns++, rows);
                GridPane.setMargin(addPostBox, new Insets(10));
                for (Post i : posts) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(FeedController.class.getResource("post.fxml"));
                    VBox postBox = fxmlLoader.load();
                    PostController postController = fxmlLoader.getController();
                    postController.setPost(i);
                    postController.setParts(i);
//                    postController.setData(posts.get(i));
                    if (columns == 1) {
                        columns = 0;
                        rows++;
                    }
                    postListView.add(postBox, columns++, rows);
                    GridPane.setMargin(postBox, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
