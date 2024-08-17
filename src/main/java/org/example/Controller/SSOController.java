package org.example.Controller;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;

public class SSOController {
    public static String sendAccessTokenRequest(String AuthCode) throws IOException, InterruptedException {
        String tokenUrl = "https://github.com/login/oauth/access_token";
        String clientId = "Ov23liNK8crEAR6uV5GD";
        String clientSecret = "10836df01b921cafa8d9d0ec79e6dbf29aec5a29";
        String redirectUri = "http://localhost/callback";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&code=" + AuthCode +
                                "&redirect_uri=" + redirectUri))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

// Extract the access token from the response
        String accessToken = new JSONObject(responseBody).getString("access_token");
        return accessToken;
    }

    public static String getUserInfo(String accessToken) throws IOException, InterruptedException {
        String userApiUrl = "https://api.github.com/user";

        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(userApiUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(userResponse.body());
        return userResponse.body();

    }

    public static void sendTokenToClient(String response) {
        try (ServerSocket serverSocket = new ServerSocket(8887)) {
            System.out.println(LocalTime.now());
            System.out.println("Server is listening on port 8887");

            // Wait for a client to connect
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Create output stream to send data to the client
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(response);

            System.out.println("Message sent to the client : " + response);

            // Close the socket after sending the message
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
