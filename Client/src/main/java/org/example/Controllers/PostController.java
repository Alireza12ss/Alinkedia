package org.example.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.Model.Post;
import org.example.Model.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.LoginController.*;
import static org.example.Controllers.OthersProfileController.emailOthers;
import static org.example.Controllers.OthersProfileController.followed;
import static org.example.Controllers.ParentController.transfer;

public class PostController {
    boolean liked;
    Post post ;
    User user;
    User poster;

    public void setPost(Post post) {
        this.post = post;
    }

    @FXML
    private ToggleButton like;

    @FXML
    private Label date;

    @FXML
    private Button followButton;

    @FXML
    private ImageView likeImage;

    @FXML
    private VBox postNode;

    @FXML
    private ImageView picture;

    @FXML
    private GNAvatarView profileImage;


    @FXML
    private TextArea text;

    @FXML
    private Button Poster;

    @FXML
    private ImageView followPic;



    private String sendGetLikeRequest(int postId) throws IOException {
        URL obj = new URL(url + "/post/" + postId + "/like");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", readFromFile());
        connection.setReadTimeout(10000); // 10 seconds

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

        return response.toString();
    }

    private ArrayList<User> likers(String tmp){
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
    @FXML
    protected void doLike() throws IOException {
        if (!liked){
            sendPostLikeRequest(post.getPostId());
            likeImage.setImage(new Image(getClass().getResource("/pictures/like.png").toString()));
            int tmp = Integer.valueOf(like.getText()) + 1;
            like.setText(String.valueOf(tmp));
            liked = true;
        }
        else {
            liked = false;
            sendPostLikeRequest(post.getPostId());
            likeImage.setImage(new Image(getClass().getResource("/pictures/unlike.png").toString()));
            int tmp = Integer.valueOf(like.getText()) - 1;
            like.setText(String.valueOf(tmp));
        }
    }

    @FXML
    protected void follow() throws IOException {
        int response = sendFollowRequest(poster.getEmail());
        if (response == 200){
            followPic.setImage(new Image(getClass().getResource("/pictures/tic.png").toString()));
            followButton.setText("Following");
        }else if (response == 201){
            followPic.setImage(new Image(getClass().getResource("/pictures/plus.png").toString()));
            followButton.setText("Follow");
        }
    }

    public static int sendFollowRequest(String email) throws IOException {
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
    private void initialize() throws IOException {
        user = GetUserWithTokenTemp(readFromFile());
    }

    private String sendPostLikeRequest(int postId) throws IOException {
        URL obj = new URL(url + "/post/" + postId + "/like");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        System.out.println(obj);
        // Setting the request method to GET
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", readFromFile());

        // Getting the response code
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);



            // Printing the response


            StringBuilder response = new StringBuilder();
            if (responseCode == 200) {
                // Reading the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            System.out.println("Response: " + response);
            connection.disconnect(); // This is okay now

            return String.valueOf(response);
        } catch (IOException e) {
            return "0";
        }
    }

    public void setParts(Post post) throws IOException {
        poster = getPoster(post.getUserId());
        if (poster.getLastName().equals(user.getLastName())) {
            followButton.setVisible(false);
        }else {
            followPic.setImage(new Image(getClass().getResource("/pictures/tic.png").toString()));
            followButton.setText("Following");
        }
        if (poster.getImagePathProfile() == null || poster.getImagePathProfile().equals("")) {
            postNode.getChildren().remove(picture);
        }else{
            System.out.println(poster.getImagePathProfile());
            profileImage.setImage(new Image(getClass().getResource("/pictures/" + poster.getImagePathProfile()).toString()));
        }
        date.setText(post.getDate() + " , " + post.getTime());
        text.setText(post.getText());
        Poster.setText(poster.getFirstName() + " " + poster.getLastName());
        if (post.getMediaPath() == null || post.getMediaPath().equals("")) {
            postNode.getChildren().remove(picture);
        }else{
            System.out.println(post.getMediaPath());
            picture.setImage(new Image(getClass().getResource("/pictures/" + post.getMediaPath()).toString()));
        }
        like.setText(String.valueOf(post.getLikes()));
        if (likers(sendGetLikeRequest(post.getPostId())).contains(user)){
            liked = true;
            likeImage.setImage(new Image(getClass().getResource("/pictures/like.png").toString()));
        }else {
            likeImage.setImage(new Image(getClass().getResource("/pictures/unlike.png").toString()));
            liked = false;
        }

    }

    private User getPoster(int userId) throws IOException {
        URL obj = new URL(url + "/post/" + userId + "/user");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        System.out.println(obj);
        // Setting the request method to GET
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", readFromFile());

        // Getting the response code
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);



            // Printing the response


            StringBuilder response = new StringBuilder();
            if (responseCode == 200) {
                // Reading the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            System.out.println("Response: " + response);
            connection.disconnect(); // This is okay now
            return createUserFromJson(String.valueOf(response));
        } catch (IOException e) {
            return null;
        }
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
    protected void goOtherProfile(ActionEvent event){
        emailOthers = poster.getEmail();
        if (followButton.getText().equals("Following")){
            followed = true;
        }else {
            followed = false;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("othersProfile.fxml"));
        transfer(fxmlLoader, String.valueOf(getClass().getResource("/cssFiles/profileStyle.css")) , event);
    }
}
