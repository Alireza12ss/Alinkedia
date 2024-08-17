package org.example.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.Model.Post;
import org.example.Model.User;
import org.example.Model.Comment;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static org.example.Controllers.FeedController.createPostFromJson;
import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.LoginController.readFromFile;
import static org.example.Controllers.ParentController.*;
import static org.example.Controllers.ProfileController.sendGetPostRequest;

public class OthersProfileController {
    static boolean followed;
    static String emailOthers;
    static User user;
    static User logUser;
    @FXML
    private ImageView background;

    @FXML
    private Button connect;

    @FXML
    private ImageView connectImage;

    @FXML
    private Label description;

    @FXML
    private ToggleButton follow;

    @FXML
    private ImageView followImage;

    @FXML
    private Button message;

    @FXML
    private GridPane postListView;

    @FXML
    private GNAvatarView profile;

    @FXML
    private Label title;


    @FXML
    private void initialize() throws IOException {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", emailOthers);
        JwtGenerator generator = new JwtGenerator();
        String token = JwtGenerator.createToken(claims, 60);
        user = LoginController.GetUserWithTokenTemp(token);
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


        String response = sendGetPostRequest(token);
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
        if (followed) {
            followImage.setImage(new Image(getClass().getResource("/pictures/tic.png").toString()));
            follow.setText("Following");
        }else {
            followImage.setImage(new Image(getClass().getResource("/pictures/plus.png").toString()));
            follow.setText("Follow");
        }
        if (isConnected(user.getEmail()) == 200){
            connect.setText("Connected");
        }else if (isConnected(user.getEmail()) == 201){
            connect.setText("Requested");
        }else if (isConnected(user.getEmail()) == 203) {
            connect.setText("Connect");
        }

    }

    private void addToList(ArrayList<Post> posts){
        // Update the UI on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            int columns = 0;
            int rows = 1;
            postListView.getChildren().clear();
            try {
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

    @FXML
    protected void pressConnect() throws IOException {
        //consider previous status of connection
        if (sendConnectRequest() == 200){
            connect.setText("Request Sent");
            connectImage.setImage(new Image(getClass().getResource("/pictures/whiteConnection.png").toString()));
        }else if (sendConnectRequest() == 201) {
            connect.setText("Connection Deleted");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            connect.setText("Connect");
        }else {
            connect.setText("Request Failed");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            connect.setText("Connect");
        }

//        //if connected
//        connect.setText("Connected");
//        connect.setStyle("-fx-background-color: indigo;" +
//                "-fx-text-fill: white");
//        connectImage.setImage(new Image(getClass().getResource("/pictures/whiteConnection.png").toString()));

//        //if not connected
//        connect.setText("Connect");
//        connect.setStyle("-fx-background-color: transparent;" +
//                "-fx-text-fill: black");
//        connectImage.setImage(new Image(getClass().getResource("/pictures/connection.png").toString()));

        //for in progress
        connect.setText("Request sent");
        connectImage.setImage(new Image(getClass().getResource("/pictures/clock.png").toString()));
    }

    private int sendConnectRequest() throws IOException {
        URL obj = new URL(url + "/connect");
        JsonObject jsonObjectq = new JsonObject();
        jsonObjectq.addProperty("receiverEmail", user.getEmail());
        jsonObjectq.addProperty("description" , "");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.addRequestProperty("Authorization", readFromFile());
        // Enable output and write JSON data
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonObjectq.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = 0;
        int retries = 3;
        while (retries > 0) {
            try {
                responseCode = connection.getResponseCode();
                break;
            } catch (IOException e) {
                System.out.println("Error getting response code: " + e.getMessage());
                e.printStackTrace();
                retries--;
                if (retries == 0) {
                    throw new IOException("Failed to get response code after retries", e);
                }
            }
        }

        System.out.println("Response Code: " + responseCode);
        if (responseCode/100 != 2){
            return responseCode;
        }

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to get input stream", e);
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response: " + response);

        return responseCode;
    }

    public static @NotNull String getString() {
        String tmp = user.getFirstName() == null ? "" : user.getFirstName();
        tmp = tmp.concat(user.getAdditionalName() == null ? " " : " " + user.getAdditionalName());
        tmp = tmp.concat(user.getLastName() == null ? " " : " " + user.getLastName() + "\n");
        tmp = tmp.concat(user.getTitle() == null ? "" : user.getTitle() + "\n");
        tmp = tmp.concat(user.getCountry() == null ? "" : user.getCountry() + " , ");
        tmp = tmp.concat(user.getCity() == null ? "" : user.getCity() + "\n");
        return tmp;
    }

    private int isConnected(String Email) throws IOException {
        URL obj = new URL(url + "/connect/ask/" + Email);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization", readFromFile());
        // Enable output and write JSON data


        int responseCode = 0;
        int retries = 3;
        while (retries > 0) {
            try {
                responseCode = connection.getResponseCode();
                break;
            } catch (IOException e) {
                System.out.println("Error getting response code: " + e.getMessage());
                e.printStackTrace();
                retries--;
                if (retries == 0) {
                    throw new IOException("Failed to get response code after retries", e);
                }
            }
        }

        System.out.println("Response Code: " + responseCode);
        if (responseCode/100 != 2){
            return responseCode;
        }

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to get input stream", e);
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response: " + response);

        return responseCode;
    }

    @FXML
    protected void pressFollow() throws IOException {
        int response = sendFollowRequest(user.getEmail());
        if (response == 200){
            followImage.setImage(new Image(getClass().getResource("/pictures/tic.png").toString()));
            follow.setText("Following");
        }else if (response == 201){
            followImage.setImage(new Image(getClass().getResource("/pictures/plus.png").toString()));
            follow.setText("Follow");
        }
    }

    private int sendFollowRequest(String email) throws IOException {
        URL obj = new URL(url + "/follow/" + email);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", readFromFile());


        int responseCode = 0;
        int retries = 3;
        while (retries > 0) {
            try {
                responseCode = connection.getResponseCode();
                break;
            } catch (IOException e) {
                System.out.println("Error getting response code: " + e.getMessage());
                e.printStackTrace();
                retries--;
                if (retries == 0) {
                    throw new IOException("Failed to get response code after retries", e);
                }
            }
        }

        System.out.println("Response Code: " + responseCode);

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to get input stream", e);
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response: " + response);

        return responseCode;
    }


    @FXML
    protected void pressMessage(ActionEvent event){
        //go to message section
    }

}
