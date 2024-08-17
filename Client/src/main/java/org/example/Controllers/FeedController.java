package org.example.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.Model.Comment;
import org.example.Model.Post;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.LoginController.*;
import static org.example.Controllers.ParentController.*;


public class FeedController {
    User user;
    @FXML
    private GridPane postListView;

    @FXML
    private Label title;

    @FXML
    private VBox list;

    @FXML
    private ImageView backProf;

    @FXML
    private Label description;

    @FXML
    private Label name;


    @FXML
    private GNAvatarView userProf;



    public String sendGetPostRequest() throws IOException {
        URL obj = new URL(url + "/feed");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Setting the request method to POST
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", readFromFile());
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

    public static Post createPostFromJson(String json){
        JSONObject jsonObject = new JSONObject(json);
        int postId = jsonObject.isNull("postId") ? null : jsonObject.getInt("postId");
        int likes = jsonObject.isNull("likes") ? null : jsonObject.getInt("likes");
        int comments = jsonObject.isNull("comments") ? null : jsonObject.getInt("comments");
        int userId = jsonObject.isNull("userId") ? null : jsonObject.getInt("userId");
        String text = jsonObject.isNull("text") ? null : jsonObject.getString("text");
        String date = jsonObject.isNull("date") ? null : jsonObject.getString("date");
        String mediaPath = jsonObject.isNull("mediaPath") ? null : jsonObject.getString("mediaPath");
        String time = jsonObject.isNull("time") ? null : jsonObject.getString("time");
        return new Post(postId , likes , comments , userId , text , Date.valueOf(date), Time.valueOf(time) , mediaPath);
    }
    private Comment createCommentFromJson(String json){
        JSONObject jsonObject = new JSONObject(json);
        int postId = jsonObject.isNull("postId") ? null : jsonObject.getInt("postId");
        int userId = jsonObject.isNull("userId") ? null : jsonObject.getInt("userId");
        int commentId = jsonObject.isNull("commentId") ? null : jsonObject.getInt("commentId");
        String text = jsonObject.isNull("text") ? null : jsonObject.getString("text");
        String mediaPath = jsonObject.isNull("mediaPath") ? null : jsonObject.getString("mediaPath");
        String date = jsonObject.isNull("date") ? null : jsonObject.getString("date");
        String time = jsonObject.isNull("time") ? null : jsonObject.getString("time");
        return new Comment(postId , userId , commentId , text , Date.valueOf(date), Time.valueOf(time) , mediaPath);

    }
    private void addToUserArray(JSONObject jsonObject , ArrayList<User> users){
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(String.valueOf(jsonObject)).getAsJsonArray();
        if (!jsonArray.isEmpty()) {
            for (JsonElement e : jsonArray) {
                users.add(createUserFromJson(e.toString()));
            }
        }
    }
    private void addToCommentArray(JSONObject jsonObject , ArrayList<Comment> users){
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(String.valueOf(jsonObject)).getAsJsonArray();
        if (!jsonArray.isEmpty()) {
            for (JsonElement e : jsonArray) {
                users.add(createCommentFromJson(e.toString()));
            }
        }
    }

    @FXML
    public void initialize() throws IOException {
        user = GetUserWithTokenTemp(readFromFile());
        if (user.getImagePathProfile() == null){
            userProf.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            userProf.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathProfile()).toString()));
        }
        description.setText(user.getTitle());
        name.setText(user.getFirstName() + " " + user.getLastName());
        String response = sendGetPostRequest();
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



    @FXML
    protected void search(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("search.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/feedStyle.css")) , event);
    }
    private void addToList(ArrayList<Post> posts){
        // Update the UI on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            int columns = 0;
            int rows = 1;
            postListView.getChildren().clear();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(FeedController.class.getResource("addPost.fxml"));
                VBox addPostBox = fxmlLoader.load();
                postListView.add(addPostBox, columns++, rows);
                GridPane.setMargin(addPostBox, new Insets(10));
                for (Post i : posts) {
                    FXMLLoader fxmlLoaderq = new FXMLLoader();
                    fxmlLoaderq.setLocation(FeedController.class.getResource("post.fxml"));
                    VBox postBox = fxmlLoaderq.load();
                    PostController postController = fxmlLoaderq.getController();
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
