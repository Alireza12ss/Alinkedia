package org.example.Controllers;

import com.google.gson.JsonObject;
import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Model.Post;
import org.example.Model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.example.Controllers.LoginApplication.url;
import static org.example.Controllers.LoginController.GetUserWithTokenTemp;
import static org.example.Controllers.LoginController.readFromFile;
import static org.example.Controllers.ParentController.goToFeed;

public class AddPostController {
    User user;
    @FXML
    private Button addMedia;

    @FXML
    private Button cancel;

    @FXML
    private Button done;

    @FXML
    private ImageView media;

    @FXML
    private VBox postNode;

    @FXML
    private TextArea postText;

    @FXML
    private GNAvatarView profPic;

    @FXML
    public void initialize() throws IOException {
        user = GetUserWithTokenTemp(readFromFile());
        postNode.getChildren().remove(media);
        if (user.getImagePathProfile() == null){
            profPic.setImage(new Image(getClass().getResource("/pictures/defaultProf.png").toString()));
        }else {
            profPic.setImage(new Image(getClass().getResource("/pictures/" + user.getImagePathProfile()).toString()));
        }
        cancel.setVisible(false);
    }

    @FXML
    protected void addPost(ActionEvent event) throws IOException {
        String urlString;
        String mediaPath = "";
        if (cancel.isVisible()) {
            urlString = media.getImage().getUrl();
            // Extract the file name from the URL
            try {
                URL url = new URL(urlString);
                mediaPath = new File(url.getPath()).getName();
                System.out.println("Image name: " + mediaPath);
                saveFile(media.getImage() , mediaPath);
            } catch (Exception e) {
                System.out.println("Error extracting image name: " + e.getMessage());
            }
        }else {
            mediaPath = "";
        }
        Post pp = new Post(postText.getText() , mediaPath);
        sendRequest(pp);
        goToFeed(event);
    }
    private void sendRequest(Post pp) throws IOException {
        JsonObject jsonObjectq = new JsonObject();
        jsonObjectq.addProperty("text" , pp.getText());
        jsonObjectq.addProperty("mediaPath" , pp.getMediaPath());

        URL serverUrl = new URL(url + "/post");
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        // Set request method
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", readFromFile());

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

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response: " + response);

    }

    private String saveFile(Image image , String name){
        // Get image dimensions
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Get PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // Create a BufferedImage
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Read pixel data into BufferedImage
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        byte[] buffer = new byte[width * height * 4]; // 4 bytes per pixel (RGBA)
        pixelReader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        // Set pixel data to BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = (y * width + x) * 4;
                int argb = ((buffer[i + 3] & 0xFF) << 24) | // Alpha
                        ((buffer[i + 2] & 0xFF) << 16) | // Red
                        ((buffer[i + 1] & 0xFF) << 8)  | // Green
                        (buffer[i] & 0xFF);             // Blue
                bufferedImage.setRGB(x, y, argb);
            }
        }

        // Save to file
        File outputFile = new File("demoLogin\\src\\main\\resources\\pictures\\" + name);
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Image saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
        System.out.println("revale");
        return "OK";
    }


    @FXML
    protected void selectMedia(ActionEvent event) throws MalformedURLException {
        String path;
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            path = String.valueOf(file.toURL());
        else
            return;
        media.setImage(new Image(String.valueOf(path)));
        media.setFitWidth(596);
        media.setFitHeight(600);
        postNode.getChildren().add(1, media);
        cancel.setVisible(true);
    }

    @FXML
    protected void cancelAction(){
        postNode.getChildren().remove(media);
        cancel.setVisible(false);
    }
}
